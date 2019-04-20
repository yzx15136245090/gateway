package com.shaopeng.gateway.dto;

import com.shaopeng.gateway.constants.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Create by liushaopeng on 2019/4/2  19:13
 **/
@Getter
@Setter
public class Result<T> implements Serializable {

    private int code;

    private String message;

    private T data;
    public Result() {
    }
    public Result(T data) {
        code = 200;
        this.data = data;
    }
    public void error(ErrorCode errorCode){
        this.code =errorCode.getCode();
        this.message = errorCode.getDesc();
    }
    public void error(int code,String message){
        this.code =code;
        this.message = message;
    }


}
