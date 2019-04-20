package com.shaopeng.gateway.framework.handler;

import com.google.common.base.Joiner;
import com.shaopeng.gateway.dto.Api;
import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.dto.Result;
import com.shaopeng.gateway.framework.hystrix.DubboHystrixCommand;

/**
 * Create by liushaopeng on 2019/4/2  12:41
 **/
public class HystrixHandler implements Handler {
    @Override
    public boolean handler(Context context) {
        Api apiInfo = context.getApiInfo();
        String threadPoolKey = Joiner.on("_").join(apiInfo.getRpcInterface(), apiInfo.getRpcMethod(), apiInfo.getRpcVersion());
        String commandKey = Joiner.on("_").join("GW", apiInfo.getRpcInterface(), apiInfo.getRpcMethod());
        Result result = new DubboHystrixCommand(context,"GW",commandKey,threadPoolKey ).execute();
        context.setResult(result);
        return true;
    }
}
