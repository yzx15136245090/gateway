package com.shaopeng.gateway.framework.plugin;

import java.util.Map;

/**
 * Create by liushaopeng on 2019/4/14  11:20
 **/
public interface SignPlugin {
    String sign(Map<String,Object> rowMap,String sign_key);
}
