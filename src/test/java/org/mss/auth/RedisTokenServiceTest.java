package org.mss.auth;

import org.junit.Test;
import org.mss.auth.model.TokenInfo;
import org.mss.auth.model.User;
import org.mss.auth.service.RedisJwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

public class RedisTokenServiceTest extends RedisTest {
    @Autowired
    private RedisJwtTokenService redisJwtTokenService;

    @Test
    public void saveKey() {
        TokenInfo tokenInfo = TokenInfo.builder()
                .accessToken(UUID.randomUUID().toString())
                .expireAt(LocalDateTime.now()
                        .plusMinutes(10)
                        .plusSeconds(10)
                        .toInstant(ZoneOffset.UTC)
                        .toEpochMilli())
                .refreshToken(UUID.randomUUID().toString())
                .refreshTokenExpireAt(LocalDateTime.now()
                        .plusMinutes(10)
                        .toInstant(ZoneOffset.UTC)
                        .toEpochMilli())
                .tokenType("bearer")
                .build();
        User user = new User();
        user.setEmail("email@gmail.com" + UUID.randomUUID().toString());
        user.setId(Long.valueOf(1L));
        user.setRoles(null);
        this.redisJwtTokenService.saveKey(tokenInfo, user);
    }

    @Test
    public void getKey() {
        System.out.println(this.redisJwtTokenService.getTokenInfo("access:c44dd00c-079f-4ff1-90be-752964f13d5b"));
    }
}
