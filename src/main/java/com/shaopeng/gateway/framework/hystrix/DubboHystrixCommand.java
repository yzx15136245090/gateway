package com.shaopeng.gateway.framework.hystrix;

import com.google.common.collect.Lists;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.shaopeng.gateway.constants.ErrorCode;
import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.dto.Result;
import com.shaopeng.gateway.framework.business.GenericDubboBusiness;
import io.vavr.control.Option;

import java.util.List;
import java.util.Map;

/**
 * Create by liushaopeng on 2019/4/3  9:26
 **/
public class DubboHystrixCommand extends HystrixCommand<Result> {

    /**
     * 分组key
     */
    private String groupKey;

    /**
     * 指定key
     */
    private String commondKey;

    /**
     * 线程池key
     */
    private String threadPoolKey;

    /**
     * 请求上下文
     */
    private Context context;

    public DubboHystrixCommand(Context context, String groupKey, String commondKey,
                                  String threadPoolKey) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commondKey))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPoolKey)));
        this.groupKey = groupKey;
        this.commondKey = commondKey;
        this.threadPoolKey = threadPoolKey;
        this.context = context;
    }

    @Override
    protected Result run() throws Exception {
        GenericDubboBusiness genericDubboBusiness = context.getIocContext().getBean(GenericDubboBusiness.class);
        Map<String, Object> rawRequest = context.getRawRequest();
        String[] methodParmaNames = Option.of(context.getApiInfo().getRpcMethodParamName())
                   .flatMap(e -> Option.of(e.split(","))).getOrNull();
        //param
        if(null != methodParmaNames){
            List<Object> paramList = Lists.newArrayList();
            for(String methodParam:methodParmaNames){
                paramList.add(rawRequest.get(methodParam));
            }
            context.setParam(paramList);
        }
        return new Result(genericDubboBusiness.doBusiness(context));
    }

    @Override
    protected Result getFallback() {
        Result result = new Result();
        result.error(ErrorCode.API_SERVICE_INVOKE_FAILD);
        return result;
    }

}
