package com.shaopeng.gateway.framework.plugin;

import com.shaopeng.gateway.utils.SignUtil;
import io.vavr.control.Try;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Create by liushaopeng on 2019/4/14  11:25
 **/
public class MD5SignPlugin implements SignPlugin {
    @Override
    public String sign(Map<String, Object> rowMap, String sign_key) {
        String verifyStr = SignUtil.getVerifyStr(rowMap, sign_key);
        return  Try.of(()->{
            return  SignUtil.bytesToHexString(encryptMD5(verifyStr.getBytes()));
        }).getOrElse("MD5");
    }

    /**
     * MD5加密
     */
    public  byte[] encryptMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }
}
