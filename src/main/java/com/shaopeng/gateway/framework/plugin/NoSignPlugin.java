package com.shaopeng.gateway.framework.plugin;

import java.util.Map;

/**
 * Create by liushaopeng on 2019/4/14  12:49
 **/
public class NoSignPlugin implements SignPlugin {
    @Override
    public String sign(Map<String, Object> rowMap, String sign_key) {
        return null;
    }
}
