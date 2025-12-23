package com.mule.demo.common;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final byte[] KEY = "teamsync-secret-key".getBytes();
    /*
    生成token
     */
    public static String createToken(Long userId,String username) {
      Map<String, Object> paylod = new HashMap<>();
      paylod.put("userId", userId);
      paylod.put("username", username);
      paylod.put("exp", System.currentTimeMillis()+1000*60*60*24);
      return JWT.create()
              .addPayloads(paylod)
              .setKey(KEY)
              .sign();
    }
   public static boolean validate(String token) {
return JWTUtil.verify(token, KEY)&& JWT.of(token).setKey(KEY).validate(0);
   }

   public static Long getUserId(String token) {
 return Long.valueOf(JWTUtil.parseToken(token).getPayload("userId").toString());
 }

}
