package com.mule.demo.service;

public interface RedisService {
    void saveToken(String token, Long userId);
    Long getUserId(String token);
    void deleteToken(String token);
}
