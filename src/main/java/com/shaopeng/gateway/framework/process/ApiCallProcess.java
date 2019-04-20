package com.shaopeng.gateway.framework.process;

import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.framework.handler.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by liushaopeng on 2019/4/1  20:27
 **/
@Component
public class ApiCallProcess implements ApplicationContextAware {

    private List<Handler> handlerChain = new ArrayList<>();

    private ApplicationContext iocContext;

    @PostConstruct
    private void  init(){

       this.addHandler(new DataParseHandler())
               .addHandler(new ParseConfigHandler())
               .addHandler(new ParamCheckHandler())
               .addHandler(new LimitHandler())
               .addHandler(new AuthHandler())
               .addHandler(new HystrixHandler())
               .addHandler(new LimitHandler.ReleaseSemaphoreHandler());

    }

    public void call(Context context){
        context.setIocContext(iocContext);
        for (Handler handler:handlerChain) {
            boolean success = handler.handler(context);
            if(!success){
                return ;
            }
        }
    }
    private ApiCallProcess addHandler(Handler handler){
        handlerChain.add(handler);
        return this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.iocContext = applicationContext;
    }
}
