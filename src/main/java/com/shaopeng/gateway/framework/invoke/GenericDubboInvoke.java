package com.shaopeng.gateway.framework.invoke;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.shaopeng.gateway.constants.Constants;
import com.shaopeng.gateway.dto.Api;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * dubbo泛化调用
 *
 * Create by liushaopeng on 2019/3/25  19:38
 **/
@Service
public class GenericDubboInvoke {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ApplicationConfig application;
    @Resource
    private RegistryConfig registry;

    private Map<String, ReferenceConfig<GenericService>> referenceCache = new ConcurrentHashMap<>();

    private Object syncObj = new Object();


    /**
     * 缓存ReferenceConfig配置引用对象
     *
     * @param apiConfig
     * @return
     */
    private ReferenceConfig getOrInitReference(Api apiConfig) {
        String referenceKey = apiConfig.getRpcInterface() + ":"  + apiConfig.getRpcVersion() + ":"  + String.valueOf(apiConfig.getRpcTimeout());
        ReferenceConfig<GenericService> referenceConfig = referenceCache.get(referenceKey);
        if (referenceConfig != null && referenceConfig.get() != null) {
            return referenceConfig;
        }

        synchronized (syncObj) {
            referenceConfig = referenceCache.get(referenceKey);
            if (referenceConfig != null && referenceConfig.get() != null) {
                return referenceConfig;
            }
            // 初始化ReferenceConfig
            referenceConfig = new ReferenceConfig<>();
            referenceConfig.setApplication(application);
            referenceConfig.setInterface(apiConfig.getRpcInterface());
            Integer timeout = apiConfig.getRpcTimeout();
            if (timeout == null || timeout <= 0) {
                timeout = Constants.DEFAULT_RPC_TIMEOUT;
            }
            referenceConfig.setTimeout(timeout);
            referenceConfig.setRegistry(registry);
            referenceConfig.setVersion(apiConfig.getRpcVersion());
            referenceConfig.setGeneric(true);//声明为泛化接口
            referenceCache.put(referenceKey, referenceConfig);
            return referenceConfig;
        }
    }


    /**
     * 支持多参数的泛化调用支持
     * @param apiConfig
     * @param param
     * @return
     */
    public Object invoke(Api apiConfig, Object param) {

        ReferenceConfig<GenericService> referenceConfig = getOrInitReference(apiConfig);
        GenericService genericService = referenceConfig.get();
        try {
            if (StringUtils.isEmpty(apiConfig.getRpcParamType())) {

                Object rpcResponse = genericService.$invoke(apiConfig.getRpcMethod(),
                        new String[]{},
                        new Object[]{});
                return rpcResponse;
            } else {
                Object[] paramValus;
                if (param instanceof List) {
                    paramValus = ((List) param).toArray();
                } else {
                    paramValus = new Object[]{param};
                }
                String paramType = apiConfig.getRpcParamType();
                Object rpcResponse = genericService.$invoke(apiConfig.getRpcMethod(),
                        paramType.split(","),
                        paramValus);
                return rpcResponse;
            }
        } catch (GenericException e) {
            logger.error("泛化调用失败,apiConfig:{}, param:{}", apiConfig, param, e);
            throw e ;
        }
    }

}


