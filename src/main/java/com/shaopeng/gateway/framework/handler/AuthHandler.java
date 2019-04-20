package com.shaopeng.gateway.framework.handler;

import com.shaopeng.gateway.constants.ErrorCode;
import com.shaopeng.gateway.dto.Api;
import com.shaopeng.gateway.dto.Client;
import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.dto.Result;
import com.shaopeng.gateway.framework.factory.SignPluginFactory;
import com.shaopeng.gateway.framework.plugin.SignPlugin;
import com.shaopeng.gateway.service.ApiManager;
import io.vavr.control.Option;

import java.util.Map;

/**
 * 权限认证
 *
 * Create by liushaopeng on 2019/4/2  12:27
 **/
public class AuthHandler implements Handler {
    private static  final String signField = "sign";
    @Override
    public boolean handler(Context context) {
        Map<String, Object> rawRequest = context.getRawRequest();
        String sign = Option.of((String)rawRequest.get(signField)).getOrNull();
        Api api = context.getApiInfo();
        Client client = ApiManager.queryClient(api.getClientId());
        int signType = Option.of(api.getSignType()).getOrElse(client.getSignType());
        SignPlugin signPlugin =SignPluginFactory.getSignPlugin(signType);
                String signStr = Option.of(signPlugin.sign(rawRequest, client.getSignKey())).get();
        if(null != signStr && !signStr.equals(sign)){
            Result result = new Result();
            result.error(ErrorCode.SIGN_CHECK_FAILD);
            context.setResult(result);
            return false;
        }
        return true;
    }
}
