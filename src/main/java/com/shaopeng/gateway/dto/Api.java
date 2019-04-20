package com.shaopeng.gateway.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Create by liushaopeng on 2019/3/31  17:44
 **/
@Data
@Builder
public class Api {

    private long id;

    private String method;

    /**
     * http method(post  get)
     */
    private String httpMethod;
    /**
     * 远程调用接口
     */
    private String rpcInterface;

    /**
     * 远程调用接口方法
     */
    private String rpcMethod;

    /**
     * 远程接口超时时间
     */
    private Integer rpcTimeout;

    /**
     * 远程接口方法参数类型
     */
    private String rpcParamType;

    /**
     * 远程接口版本号
     */
    private String rpcVersion;
    /**
     * 方法的参数名称
     */
    private String rpcMethodParamName;

    /**
     * 所属client
     */
    private long clientId;
    /**
     * 加密类型
     */
    private Integer signType;

//
//    /**
//     * http请求方法
//     */
//    private String httpMethod;
}
