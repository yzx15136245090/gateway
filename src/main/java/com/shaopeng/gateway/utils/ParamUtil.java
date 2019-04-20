package com.shaopeng.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Http request中相关的工具类
 */
public class ParamUtil {

    public static long getLong(HttpServletRequest request, String key) {
        String value = getValue(request, key);
        return Long.parseLong(value);
    }

    public static String getString(HttpServletRequest request, String key) {
        String value = getValue(request, key);
        if (null == value) {
            throw new IllegalArgumentException("the param is null !");
        }
        return value;
    }

    public static int getInt(HttpServletRequest request, String key) {
        String value = getValue(request, key);
        return Integer.parseInt(value);
    }

    public static float getFloat(HttpServletRequest request, String key) {
        String value = getValue(request, key);
        return Float.valueOf(value);
    }

    private static String getValue(HttpServletRequest request, String key) {
        if (request == null || key == null) {
            throw new IllegalArgumentException("the param is null !");
        }
        String value = request.getParameter(key);
        if (null == value) {
            throw new IllegalArgumentException(key + " is null");
        }
        return value;
    }

    /**
     * 从request中获取所有的参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParams(ServletRequest request) {
        //getParameterMap的类型是Map<String,String[]>
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> allParam = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            allParam.put(key, request.getParameter(key));
        }
        handlePostParams(allParam);
        return allParam;
    }


    /**
     * 有些post请求接收到参数会很特殊
     * key是json格式，value是没有的，需要解析出来放入到map中
     *
     * @param paramMap
     * @return
     */
    private static Map<String, String> handlePostParams(Map<String, String> paramMap) {
        Map<String, String> newParamMap = new HashMap<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                String postBodyKey = entry.getKey();
                if (null == postBodyKey) {
                    continue;
                }

                try {
                    JSONObject json = JSON.parseObject(postBodyKey);
                    for (Map.Entry<String, Object> entry1 : json.entrySet()) {
                        newParamMap.put(entry1.getKey(), String.valueOf(entry1.getValue()));
                    }
                } catch (Exception e) {

                }
            } else {
                newParamMap.put(entry.getKey(), entry.getValue());
            }
        }
        paramMap.clear();
        paramMap.putAll(newParamMap);


        return paramMap;
    }

    /**
     * 获取请求的ip地址
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotEmpty(ip) && ip.contains(",")) {//多层代理取第一个
            ip = ip.split(",")[0];
        }
        return ip;
    }

}

