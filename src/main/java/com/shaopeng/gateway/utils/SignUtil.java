package com.shaopeng.gateway.utils;

import java.util.*;

/**
 * Create by liushaopeng on 2019/4/20  17:16
 **/
public class SignUtil {

    public static String getVerifyStr(Map<String, Object> params, String signKey) {
        params.remove("sign");
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

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
