package com.shaopeng.gateway.service;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.shaopeng.gateway.dao.ApiInfoDao;
import com.shaopeng.gateway.dto.Api;
import com.shaopeng.gateway.dto.ApiConfig;
import com.shaopeng.gateway.dto.Client;
import com.shaopeng.gateway.dto.LimitConfig;
import com.shaopeng.gateway.thread.ThreadContent;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

/**
 * Api所有信息管理类
 * 可以考虑存储到redis中
 *
 * Create by liushaopeng on 2019/3/31  18:21
 **/
@Service
public class ApiManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ScheduledExecutorService scheduledExecutorService = ThreadContent.defaultScheduledExecutor;

    @Resource
    private ApiInfoDao apiInfoDao;
    /* path->{API} */
    private static Map<String,Api>  apiPathMap = Maps.newConcurrentMap();
    /* apiId->{LimitConfig} */
    private static Map<Long, LimitConfig> limitConfigMap = Maps.newConcurrentMap();
    /* path->{Client} */
    private static Map<Long, Client> clientMap = Maps.newConcurrentMap();
    

    @PostConstruct
    private void init(){
        boolean isSuccess = loadCache();
        if(!isSuccess){
            throw new RuntimeException("load cached faild");
        }
        scheduledExecutorService.scheduleAtFixedRate(
                ()->{
                    loadCache();
                },120, 60, TimeUnit.SECONDS
        );

    }

    private boolean loadCache() {
       return  Try.of(()->{
            logger.info(" start load cache!");
            List<Api> apis = apiInfoDao.queryAllApi();

            apis.stream().forEach(api ->{
                apiPathMap.put(api.getMethod().trim(),api);
            });
            List<ApiConfig> apiConfigs =apiInfoDao.queryAllApiConfig();
            apiConfigs.stream().forEach(apiconfig->{
                LimitConfig limitConfig = new LimitConfig();
                limitConfig.setRateLimiter(RateLimiter.create(apiconfig.getQps()));
                limitConfig.setSemaphore(new Semaphore(apiconfig.getConcurrency()));
                limitConfigMap.put(apiconfig.getApiId(),limitConfig);
            });
           List<Client> clients = apiInfoDao.queryAllClient();
           clients.stream().forEach(client->{
               clientMap.put(client.getId(),client);
           });

           return Boolean.TRUE;
        }).recover(e->Match(e).of(
               Case($(instanceOf(Exception.class)), t -> {
                   // 有异常 则返回失败
                   logger.error("load cache failed! message:{}",t.getMessage());
                   return Boolean.FALSE;
               })
       )).get();
    }


    public static Api queryApi(String method){
        return apiPathMap.get(method.trim());
    }

    public static LimitConfig queryApi(Long apiId){
        return limitConfigMap.get(apiId);
    }

    public static Client queryClient(Long clientId){
        return clientMap.get(clientId);
    }
}
