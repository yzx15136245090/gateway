package com.shaopeng.gateway.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Create by liushaopeng on 2019/3/31  18:24
 **/
public interface ThreadContent {
    /**
     * 默认的定时任务线程
     */
     ScheduledExecutorService defaultScheduledExecutor=
             Executors.newScheduledThreadPool(1);



}
