package com.shaopeng.gateway.framework.handler;

import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.utils.ParamUtil;
import com.shaopeng.gateway.utils.RequestHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据解析
 *
 * Create by liushaopeng on 2019/4/2  12:28
 **/
public class DataParseHandler implements Handler {
    @Override
    public boolean handler(Context context) {
        HttpServletRequest request = RequestHolder.getRequest();
        context.setHttpMethod(request.getMethod());
        context.setFromIp(ParamUtil.getRemoteIp(request));

        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, Object> allParam = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            allParam.put(key, request.getParameter(key));
        }
        context.setRawRequest(allParam);
        return true;
    }
}
