package com.shaopeng.gateway.framework.plugin;

import com.shaopeng.gateway.utils.SignUtil;
import io.vavr.control.Try;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Create by liushaopeng on 2019/4/14  11:25
 **/
public class SHA1SignPlugin  implements SignPlugin{
    @Override
    public String sign(Map<String, Object> rowMap, String sign_key) {
        String verifyStr = SignUtil.getVerifyStr(rowMap, sign_key);

        return Try.of(()->{
            return encodeBySHA(verifyStr);
        }).getOrElse("SHA1");
    }
    public  String encodeBySHA(String str) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.update(str.getBytes());
        String hex = toHex(sha1.digest());
        return hex;
    }

    /**
     * sha1 摘要转16进制
     *
     * @param digest
     * @return
     */
    private static String toHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        int length = digest.length;

        for (int i = 0; i < length; i++) {
            // 原始方法
            String out = Integer.toHexString(0xFF & digest[i]);
            // 如果为1位 前面补个0
            if (out.length() == 1) {
                sb.append("0");
            }
            sb.append(out);
        }
        return sb.toString();
    }
}
