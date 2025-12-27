package com.mule.demo.service;

public interface RedisService {
    void saveToken(String token, Long userId);
    Long getUserId(String token);
    void deleteToken(String token);
    public Double getZScore(String key, Object member);
    public boolean sHasKey(String key, Object value);
    public long sSet(String key, Object... values);
    public long sRemove(String key, Object... values);
    public Double zIncr(String key, Object member, double score);
}
