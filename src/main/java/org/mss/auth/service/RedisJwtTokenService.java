package org.mss.auth.service;

import org.mss.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisJwtTokenService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void add(User user) {
        this.redisTemplate.opsForList().leftPush("key", user);
    }
}
