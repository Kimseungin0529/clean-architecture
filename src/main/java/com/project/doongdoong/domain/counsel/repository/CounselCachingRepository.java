package com.project.doongdoong.domain.counsel.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class CounselCachingRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void incrementValue(String key, int integer){
        redisTemplate.opsForValue().increment(key, integer);
    }

    public void expire(String key, long timeout, TimeUnit timeUnit){
        redisTemplate.expire(key, timeout, timeUnit);
    }
}

