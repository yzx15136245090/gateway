package com.shaopeng.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.shaopeng.gateway.constants.ErrorCode;
import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.dto.Result;
import com.shaopeng.gateway.framework.process.ApiCallProcess;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * Create by liushaopeng on 2019/3/26  8:54
 **/
@RestController
public class ApiController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ApiCallProcess  apiCallProcess;

    @RequestMapping("/**/**")
    public Object api(){
        return Try.of(()->{
            Context context = new Context();
            apiCallProcess.call(context);
            return context.getResult();
        }).onFailure(e->{
            logger.error("api run faild!! result:{}", JSON.toJSONString(e));
        }).getOrElse(()->{
            Result result = new Result();
            result.error(ErrorCode.HYSTRIX_FALLBACK);
            return result;
        });
    }

}
