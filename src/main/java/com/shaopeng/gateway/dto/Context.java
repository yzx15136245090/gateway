package com.shaopeng.gateway.dto;

import com.shaopeng.gateway.framework.plugin.SignPlugin;
import lombok.Data;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文传参
 *
 * Create by liushaopeng on 2019/3/31  21:52
 **/
@Data
public class Context {

    //请求方式（get or post）
    private String httpMethod;
    //请求path
    private String uriPath;
    //请求来源IP
    private String fromIp;
    //前端参数
    private Map<String,Object>  rawRequest;
    //向下穿透参数
    private Object param;
    //api info
    private Api apiInfo;

    //处理过程中需要往后传递的数据,比如转换后的参数，api配置,结果对象等,释放信号量
    private Map<String, Object> shareObjects = new HashMap<String, Object>();

    //result
    private Object result;
    //ioc容器
    private ApplicationContext iocContext;
}
