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
                 .addPathPatterns("/**") 
                 .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/favicon.ico",
                        "/",
                        "/index.html",
                        "/static/**",
                        "/js/**",
                        "/css/**"
            );
     }
}
