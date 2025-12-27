package com.mule.demo.service.impl;

import com.mule.demo.service.RedisService;

import org.simpleframework.xml.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String TOKEN_PREFIX = "login:token:";

    @Override
    public void saveToken(String token, Long userId) {
        redisTemplate.opsForValue().set(TOKEN_PREFIX + token, userId, 24, TimeUnit.HOURS);
    }

    @Override
    public Long getUserId(String token) {
        Object val = redisTemplate.opsForValue().get(TOKEN_PREFIX + token);
        return val == null ? null : Long.valueOf(val.toString());
    }

    @Override
    public void deleteToken(String token) {
        redisTemplate.delete(TOKEN_PREFIX + token);
    }
    @Override
    public Double getZScore(String key, Object member) {
        return redisTemplate.opsForZSet().score(key, member);
    }
    @Override
    public boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }
    @Override
    public long sSet(String key, Object... values) {
        try{
            return redisTemplate.opsForSet().add(key, values);
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public long sRemove(String key, Object... values) {
        try{
            return redisTemplate.opsForSet().remove(key, values);
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
    }
}
    @Override
    public Double zIncr(String key, Object member, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, member, score);
    }
}
