package com.shaopeng.gateway.constants;

/**
 * Create by liushaopeng on 2019/4/20  16:49
 **/
public enum ErrorCode {
    //请求错误401-450
    API_NOT_FOUND(401,"api not found!!"),
    METHOD_NOT_SUPPORT(402,"api httpMethod not supported"),
    REQUEST_TIMEOUT(403, "request timeout"),
    SIGN_CHECK_FAILD(404,"sign check failed!!"),


    //配置错误451-499
    API_SERVICE_INVOKE_FAILD(451,"api service error!!"),

    QPS_LIMITED(461, "qps超过限制"),
    CONCURENCY_LIMITED(462, "并发超过限制"),
    HYSTRIX_FALLBACK(463, "请稍后再试");

    private int code;
    private String desc;
    ErrorCode(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
