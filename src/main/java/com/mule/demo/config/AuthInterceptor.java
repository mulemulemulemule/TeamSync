package com.mule.demo.config;

import com.mule.demo.common.UserContext;
import com.mule.demo.exception.ServiceException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import com.mule.demo.common.JwtUtils;
public class AuthInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, Object> redisTemplate;
    public AuthInterceptor(RedisTemplate<String, Object> redisTemplate) {
           this.redisTemplate = redisTemplate;
       }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token==null||token.isEmpty()) {
            throw new ServiceException(401, "plz login first");
        }
        String rediskey = "login:token:" + token;
        Object userId= redisTemplate.opsForValue().get(rediskey);
        if(userId==null){
            throw new ServiceException(401, "login timeout,plz login again");
        }
    if(!JwtUtils.validate(token)){
        throw new ServiceException(401, "token invalid");
    }
    

UserContext.setUserId(Long.valueOf(userId.toString()));
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }

}
