# gateway

### 针对dubbo服务开发gateway
解决问题：作为统一的服务出口，解决多应用服务出口混乱问题

网关作用：一般也会把路由，安全，限流，缓存，日志，监控，重试，熔断等都放到 API 网关来做，然后服务层就完全脱离这些东西，纯粹的做业务，也能够很好的保证业务代码的干净，不用关心安全，压力等方面的问题

## 网关使用文档

**请求地址：** http://host:port/gateway

**公共请求参数：**

| 参数名称          | 类型     | 必选   | 说明                    |
| ------------- | ------ | ---- | --------------------- |
| method     | string | Y    | 方法名           |
| timestamp     | long   | Y    | 时间戳                   |
| sign          | string | Y    | 签名 (附录1)                  |

**其他请求参数说明：**
请求字段必须与配置方法字段一致，对象转为json赋值字段，其他按照正常赋值

**示例：** 

httpL//localhost:8090/gateway?method=com.ss.xx.addUser&timestamp=1234567890&sign=xxxxxx&user_name=xiaoming&pwd=xioaming

**表设计：**
```
CREATE TABLE `gw_api` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `api_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Api名称',
  `method` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'uri method',
  `http_method` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'http method',
  `rpc_interface` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'dubbo泛化调用类型，需配置dubbo接口全路径名',
  `rpc_param_type` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'dubbo泛化调用类型，需配置dubbo接口入参对象全路径名.目前只支持单个对象作为入参',
  `rpc_method` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'dubbo泛化调用类型,需配置dubbo服务的方法名',
  `rpc_method_param_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '方法参数名称',
  `rpc_timeout` int(13) NOT NULL DEFAULT '3000' COMMENT 'rpc超时时间(ms)',
  `rpc_version` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1.0.0' COMMENT 'dubbo泛化调用类型,dubbo服务的版本号',
  `desc` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'api描述，业务功能、注意事项等说明',
  `client_id` bigint(20) NOT NULL COMMENT 'client应用信息',
  `priority` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '优先级,数字越大优先级越低，范围 0-4',
  `sign_type` tinyint(3) DEFAULT NULL  COMMENT '签名方式(1:sha1,2:md5)',
  `is_valid` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `create_time` bigint(20) NOT NULL COMMENT '记录生成时间',
  `op_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作时间',
  `last_ver` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_api_name` (`api_name`),
  UNIQUE KEY `uk_method` (`method`),
  KEY `idx_client_id` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Api基本信息表';

CREATE TABLE `gw_client` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `client_name` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT 'Api名称',
  `sign_key` varchar(45) NOT NULL COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '秘钥',
  `sign_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '签名类型 1:sha1 2:md5',
  `is_valid` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '记录生成时间',
  `op_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作时间',
  `last_ver` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='client应用基本信息表'

CREATE TABLE `gw_api_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `api_id` bigint(20) unsigned NOT NULL COMMENT 'api关联ID',
  `qps` int(4) NOT NULL DEFAULT '300' COMMENT 'qps',
  `concurrency` int(4) NOT NULL DEFAULT '300' COMMENT '并发',
  `is_valid` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '记录生成时间',
  `op_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作时间',
  `last_ver` smallint(6) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='api配置表'
  ```
  
**网关具体实现**
* 1.限流 qps   ->RateLimiter  (google guava RateLimiter)
* 2.控制并发   ->Semaphore ( 信号量 java Semaphore)
* 3.熔断       ->Hytrix ***(为什么要用hytrix,在分布式系统中会出现很多的调用失败，比如会出现超时，异常错误，hytrix就是保证分布式系统中出现调用是啊比的情况下，不会导致整体的服务失败，Hystrix提供了熔断、隔离、Fallback、cache、监控等功能。 这里使用hytrix熔断器。)***
* 4.服务调用   ->dubbo 泛化调用 

      
 ### 附录1
 
**所有参数升序排列然后加密**

~~~
    加密字符串
    public static String getVerifyStr(Map<String, Object> params, String signKey) {
        params.remove("sign_key",signKey);
        Map<String, String> needVerify = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            needVerify.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        List<Map.Entry<String, String>> entryList = new ArrayList<>(needVerify.entrySet());
        //排序
        Collections.sort(entryList, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : entryList) {
            sb.append(entry.getKey()).append(entry.getValue());
        }
        return sb.toString();
    }
~~~
