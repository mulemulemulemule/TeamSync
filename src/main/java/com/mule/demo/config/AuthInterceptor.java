package com.mule.demo.config;

import com.mule.demo.common.JwtUtils;
import com.mule.demo.common.UserContext;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    private final RedisService redisService;

    public AuthInterceptor(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) return true;
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) throw new ServiceException(401, "Login required");
        Long uid = redisService.getUserId(token);
        if (uid == null || !JwtUtils.validate(token)) throw new ServiceException(401, "Invalid session");
        UserContext.setUserId(uid);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.remove();
    }
}