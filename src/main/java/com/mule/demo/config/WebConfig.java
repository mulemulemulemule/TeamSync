package com.mule.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 @Configuration
 public class WebConfig implements WebMvcConfigurer {
@Autowired
private RedisTemplate<String, Object> redisTemplate;
     @Override
     public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(new AuthInterceptor(redisTemplate))
                 .addPathPatterns("/**") // 拦截所有
                .excludePathPatterns(
                        "/user/login",      // 放行登录
                         "/user/register",   // 放行注册
                         "/doc.html",        // 放行文档
                        "/webjars/**",      // 放行文档资源
                         "/v3/api-docs/**",  // 放行文档资源
                        "/favicon.ico"      // 放行图标
            );
 }
}