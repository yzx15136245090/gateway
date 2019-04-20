package com.shaopeng.gateway.framework.business;

import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.framework.invoke.GenericDubboInvoke;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务执行
 *
 * Create by liushaopeng on 2019/4/2  19:17
 **/
@Component
public class GenericDubboBusiness implements BizBusiness {
    @Resource
    private GenericDubboInvoke invoke;

    @Override
    public Object doBusiness(Context context) {
        return invoke.invoke(context.getApiInfo(),context.getParam());
    }
}
