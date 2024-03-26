package com.hasd.ddcodeboot.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/11/16 16:05
 **/


public class JwtUtil {

    /**
     * @param payload HashMapM<String,Object>
     * @param secret  String
     * @return java.lang.String
     * @Description 生成Token
     * @author hasdsd
     * @Date 2023/11/16
     */
    public static String generateJwt(HashMap<String, Object> payload, String secret) {
        payload.put("expire_time", System.currentTimeMillis() + 1000L * 60 * 60 * 24);//一天
        return JWTUtil.createToken(payload, secret.getBytes());
    }

    /**
     * @param key string,
     * @return cn.hutool.jwt.JWT
     * @Description getTokenInfo
     * @author hasdsd
     * @Date 2023/11/17
     */
    public static String getTokenInfo(String token, String key) {
        return JWTUtil.parseToken(token).getPayload(key).toString();
    }


    public static List<String> getAuthorities(String token) {
        String authoritiesString = JWTUtil.parseToken(token).getPayload("authorities").toString();
        JSONArray list = JSONUtil.parseArray(authoritiesString);
        return JSONUtil.toList(list, String.class);
    }

    /**
     * @param token  string
     * @param secret String
     * @return java.lang.Boolean
     * @Description 验证JWT
     * @author hasdsd
     * @Date 2023/11/17
     */
    public static Boolean checkJwt(String token, String secret) {

        JWT jwt = JWTUtil.parseToken(token);

        String expireTime = jwt.getPayload("expire_time").toString();

        if (expireTime.isEmpty()) {
            return false;
        }

        if (Long.parseLong(expireTime) < System.currentTimeMillis()) {
            return false;
        }
        return JWTUtil.verify(token, secret.getBytes());
    }
}
