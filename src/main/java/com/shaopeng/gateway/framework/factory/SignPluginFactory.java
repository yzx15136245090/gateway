package com.shaopeng.gateway.framework.factory;

import com.shaopeng.gateway.framework.plugin.MD5SignPlugin;
import com.shaopeng.gateway.framework.plugin.NoSignPlugin;
import com.shaopeng.gateway.framework.plugin.SHA1SignPlugin;
import com.shaopeng.gateway.framework.plugin.SignPlugin;

/**
 * Create by liushaopeng on 2019/4/14  11:28
 **/
public class SignPluginFactory {

    public static SignPlugin getSignPlugin(int signType){
        SignPlugin signPlugin = null;
        switch (signType){
            case 0 : signPlugin =  new NoSignPlugin(); break;
            case 1 : signPlugin =  new SHA1SignPlugin(); break;
            case 2 : signPlugin =  new MD5SignPlugin(); break;
            default:  signPlugin =  new SHA1SignPlugin(); break;
        }
        return signPlugin;
    }
}
