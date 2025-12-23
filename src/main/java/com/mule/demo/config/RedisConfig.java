package com.mule.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

    @Configuration
    public class RedisConfig {
   
        @Bean
       public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
           template.setConnectionFactory(factory);
  
           // 设置 Key 的序列化方式 (字符串)
          template.setKeySerializer(new StringRedisSerializer());
         template.setHashKeySerializer(new StringRedisSerializer());
 
           // 设置 Value 的序列化方式 (JSON)
          GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
          template.setValueSerializer(jsonSerializer);
         template.setHashValueSerializer(jsonSerializer);
 
        return template;
      }
 }