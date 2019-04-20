package com.shaopeng.gateway.framework.handler;

import com.shaopeng.gateway.constants.ErrorCode;
import com.shaopeng.gateway.dto.Api;
import com.shaopeng.gateway.dto.Context;
import com.shaopeng.gateway.dto.Result;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 参数校验
 *
 * Create by liushaopeng on 2019/4/2  12:29
 **/
public class ParamCheckHandler implements Handler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static  final  Long REQUEST_VALID_PERIOD  = 1000 * 60 * 5L;
    @Override
    public boolean handler(Context context) {
        return Try.of(()->{
            Api apiInfo = context.getApiInfo();
            if(null == apiInfo){
                Result result = new Result();
                result.error(ErrorCode.API_NOT_FOUND);
                context.setResult(result);
                return false;
            }

            if(!apiInfo.getHttpMethod().equals(context.getHttpMethod())){
                Result result = new Result();
                result.error(ErrorCode.METHOD_NOT_SUPPORT);
                context.setResult(result);
                return false;
            }

            if(!isTimeValid(context)){
                Result result = new Result();
                result.error(ErrorCode.REQUEST_TIMEOUT);
                context.setResult(result);
                return false;
            }






            return true;
        }).onFailure(e-> logger.error("check failed!",e)).get();
    }

    /**
     * 检查时间是否有效
     * @param context
     * @return
     */
    private boolean isTimeValid(Context context) {
        Object timestampObj = context.getRawRequest().get("timestamp");
        if (null == timestampObj) {
            return false;
        }
        long timesIn = Long.parseLong(String.valueOf(timestampObj));
        long now = System.currentTimeMillis();

        return Math.abs(now - timesIn) < REQUEST_VALID_PERIOD;
    }
}
