package com.shaopeng.gateway.dto;

import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;

import java.util.concurrent.Semaphore;

/**
 * Create by liushaopeng on 2019/3/31  15:54
 **/
@Data
public class LimitConfig {

    /**
     * 限流，QPS
     */
    private RateLimiter rateLimiter;

    /**
     * 信号量，控制并发
     */
    private Semaphore semaphore;
}
