package com.shaopeng.gateway.framework.handler;

import com.google.common.util.concurrent.RateLimiter;
import com.shaopeng.gateway.constants.Constants;
import com.shaopeng.gateway.constants.ErrorCode;
import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.dto.LimitConfig;
import com.shaopeng.gateway.dto.Result;
import com.shaopeng.gateway.service.ApiManager;
import io.vavr.control.Option;

import java.util.concurrent.Semaphore;

/**
 * 限流Handler
 *
 * Create by liushaopeng on 2019/3/31  21:57
 **/
public class LimitHandler implements Handler {

    @Override
    public boolean handler(Context context) {
        Long apiId = Option.of(context.getApiInfo())
                .flatMap(api -> Option.of(api.getId()))
                .getOrNull();
        if(null == apiId){
            Result result = new Result();
            result.error(ErrorCode.QPS_LIMITED);
            context.setResult(result);
            return false;
        }
        LimitConfig limitConfig = ApiManager.queryApi(apiId);
        if(null == limitConfig){
            return true;
        }
        //qps
        RateLimiter rateLimiter = limitConfig.getRateLimiter();
        if(!rateLimiter.tryAcquire()){
            Result result = new Result();
            result.error(ErrorCode.QPS_LIMITED);
            context.setResult(result);
            return false;
        }
        //信号量控制并发
        Semaphore semaphore = limitConfig.getSemaphore();
        if(!semaphore.tryAcquire()){
            Result result = new Result();
            result.error(ErrorCode.CONCURENCY_LIMITED);
            context.setResult(result);
            return false;
        }
        //不能再此处释放，应在程序解析完成时释放
        context.getShareObjects().put(Constants.semaphore,semaphore);

        return true;
    }

    /**
     * 释放信号量
     */
    public static class ReleaseSemaphoreHandler implements Handler{

        @Override
        public boolean handler(Context context) {
            Semaphore semaphore =(Semaphore) context.getShareObjects().get(Constants.semaphore);
            if(null != semaphore){
                semaphore.release();
            }
            return true;
        }
    }
}
