package com.mule.demo.config;

import com.mule.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 @Configuration
 public class WebConfig implements WebMvcConfigurer {
     @Autowired
     private RedisService redisService;

     @Override
     public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(new AuthInterceptor(redisService))
                 .addPathPatterns("/**") 
                 .excludePathPatterns("/user/login", "/user/register", "/doc.html", "/webjars/**", "/v3/api-docs/**", "/favicon.ico", "/", "/index.html");
     }
}
