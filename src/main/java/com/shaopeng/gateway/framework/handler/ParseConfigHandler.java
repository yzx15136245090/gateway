package com.shaopeng.gateway.framework.handler;

import com.shaopeng.gateway.dto.Api;
import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.framework.factory.SignPluginFactory;
import com.shaopeng.gateway.service.ApiManager;
import io.vavr.control.Option;

import java.util.Map;

/**
 * 数据组装
 *
 * Create by liushaopeng on 2019/4/2  12:31
 **/
public class ParseConfigHandler  implements Handler  {
    private static final String methodField = "method";
    @Override
    public boolean handler(Context context) {
        Map<String, Object> rawRequest = context.getRawRequest();
        String method = (String)rawRequest.get(methodField);
        Api api = ApiManager.queryApi(method);
        context.setApiInfo(api);
        return true;
    }
}
