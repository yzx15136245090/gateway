package com.shaopeng.gateway.framework.handler;

import com.shaopeng.gateway.dto.Context;

/**
 * 程序处理器
 *
 * Create by liushaopeng on 2019/3/31  21:51
 **/
public interface Handler {
    /**
     * 处理器
     *
     * @return
     */
    boolean handler(Context context);

}
