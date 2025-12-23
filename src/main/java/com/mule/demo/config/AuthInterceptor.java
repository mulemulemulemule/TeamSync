package com.mule.demo.config;

import com.mule.demo.common.UserContext;
import com.mule.demo.exception.ServiceException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import com.mule.demo.common.JwtUtils;
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token==null||token.isEmpty()) {
            throw new ServiceException(401, "plz login first");
        }
    if(!JwtUtils.validate(token)){
        throw new ServiceException(401, "token invalid");
    }
    Long userId =JwtUtils.getUserId(token);
    UserContext.setUserId(userId);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }

}
