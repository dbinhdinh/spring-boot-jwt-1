package org.mss.auth.service;

import org.mss.auth.model.TokenInfo;
import org.mss.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Service
public class RedisJwtTokenService {
    public static final String ACCESS = "access:";
    public static final String REFRESH = "refresh:";

    private static final String TOKEN_KEY = "tkk";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Redis Expireat command is used to set the expiry of key in Unix timestamp format.
     * After the expiry time, the key will not be available in Redis.
     * @param k: Key
     * @param time:
     * @return
     */
    public Boolean expireAt(String k, @NotNull long time) {
        byte[] rawKey = this.redisTemplate.getKeySerializer().serialize(k);

        return (Boolean) this.redisTemplate.execute(connection -> {
            try {
                return connection.pExpireAt(rawKey, time);
            } catch (Exception e) {
                return connection.expireAt(rawKey, time / 1000);
            }
        }, true);
    }

    /**
     * @param tokenInfo: token to be used as a key in redis
     * @param user:      cache user info will be use for all request(id, email, granted-authorities)
     */
    public void saveKey(@NotNull TokenInfo tokenInfo, @NotNull User user) {
        Objects.requireNonNull(tokenInfo, "Token_Info can not be null.");
        Objects.requireNonNull(user, "User can not be null.");

        this.redisTemplate.opsForValue().set(ACCESS + tokenInfo.getAccessToken(), user);
        this.redisTemplate.opsForValue().set(REFRESH + tokenInfo.getRefreshToken(), tokenInfo.getAccessToken());

        this.expireAt(ACCESS + tokenInfo.getAccessToken(), tokenInfo.getExpireAt());
        this.expireAt(REFRESH + tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpireAt());
    }

    /**
     * remove key
     */

    public void removeKey(String accessToken) {

    }


    /**
     * update key ~ saveKey ?
     */

    public void updateKey(String accessToken) {

    }
}
