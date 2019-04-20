package com.shaopeng.gateway.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Create by liushaopeng on 2019/4/10  8:58
 **/
public class RequestHolder {
    private static  final  ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();

    public static HttpServletRequest getRequest(){
        return threadLocal.get();
    }

    public static void setRequest(HttpServletRequest request){
        threadLocal.set(request);
    }

    public static void clear(){
        threadLocal.remove();
    }
}
