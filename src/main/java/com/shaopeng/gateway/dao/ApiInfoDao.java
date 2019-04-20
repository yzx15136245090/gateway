package com.shaopeng.gateway.dao;

import com.shaopeng.gateway.dto.Api;
import com.shaopeng.gateway.dto.ApiConfig;
import com.shaopeng.gateway.dao.mapper.ApiInfoMapper;
import com.shaopeng.gateway.dto.Client;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create by liushaopeng on 2019/3/31  18:08
 **/
@Repository
public class ApiInfoDao {
    @Resource
    private ApiInfoMapper apiInfoMapper;

    public List<Api> queryAllApi(){
        return apiInfoMapper.queryAllApi();
    }

    public List<ApiConfig> queryAllApiConfig(){
        return apiInfoMapper.queryAllApiConfig();
    }

    public  List<Client> queryAllClient(){
        return apiInfoMapper.queryAllClient();
    }
}
