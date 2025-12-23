package com.mule.demo.common;
import cn.hutool.jwt.JWT;
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
      paylod.put("JWT.EXPIRES_AT", System.currentTimeMillis()+1000*60*60*24); // 24小时后过期
      return JWT.create()
              .addPayloads(paylod)
              .setKey(KEY)
              .sign();
    }

}
