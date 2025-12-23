package com.mule.demo.config;

import com.mule.demo.common.JwtUtils;
import com.mule.demo.common.UserContext;
import com.mule.demo.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;


public class AuthInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;

    public AuthInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 请求 (跨域预检)
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("token");
        
        // 1. 基础校验
        if (token == null || token.isEmpty() || !JwtUtils.validate(token)) {
           throw new ServiceException(401, "plz login first");
        }

        // 2. Redis 状态校验
        String redisKey = "login:token:" + token;
        Object userId = redisTemplate.opsForValue().get(redisKey);
        if (userId == null) {
            throw new ServiceException(401, "login timeout,plz login again");
        }

        // 3. 存入上下文
        UserContext.setUserId(Long.valueOf(userId.toString()));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.remove();
    }
}