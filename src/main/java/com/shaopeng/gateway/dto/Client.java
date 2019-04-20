package com.shaopeng.gateway.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Create by liushaopeng on 2019/4/20  18:40
 **/
@Data
public class Client implements Serializable {

    private Long id;

    private String clientName;

    private int signType;

    private String signKey;

}
