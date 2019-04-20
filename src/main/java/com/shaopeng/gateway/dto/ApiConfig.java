package com.shaopeng.gateway.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Create by liushaopeng on 2019/3/31  17:45
 **/
@Data
@Builder
public class ApiConfig {

    /**
     * api主键
     */
    private long apiId;

    /**
     * qps限制
     */
    private int qps;

    /**
     * 并发量限制
     */
    private int concurrency;
}
