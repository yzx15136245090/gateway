package com.shaopeng.gateway.dao.mapper;

import com.shaopeng.gateway.dto.Api;
import com.shaopeng.gateway.dto.ApiConfig;
import com.shaopeng.gateway.dto.Client;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Create by liushaopeng on 2019/3/31  17:17
 **/
public interface ApiInfoMapper {

    @Select("select id as id , method as method ,http_method as httpMethod, rpc_interface as rpcInterface,rpc_method as rpcMethod , " +
            "rpc_timeout as rpcTimeout ,rpc_param_type as rpcParamType ,rpc_version as rpcVersion,rpc_method_param_name as rpcMethodParamName," +
            "client_id as clientId , sign_type as signType from gw_api where is_valid = 1")
    List<Api> queryAllApi();

    @Select("select api_id as apiId , qps as qps , concurrency as concurrency from gw_api_config where is_valid = 1 ")
    List<ApiConfig> queryAllApiConfig();

    @Select("select id as id ,client_name as clientName , sign_type as signType , sign_key as signKey from gw_client where is_valid =1")
    List<Client> queryAllClient();
}
