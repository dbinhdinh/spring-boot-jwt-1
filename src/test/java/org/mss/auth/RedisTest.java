package org.mss.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mss.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AuthenticationApplication.class})
public class RedisTest {

    public static final String USER_KEY = "keu1";
    public static final String OTHER_KEY= USER_KEY+"-otherkey";
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void add() {
        List<User> users = IntStream.rangeClosed(0, 2)
                .parallel()
                .mapToObj(el -> {
                    User user = new User();
                    user.setId(Long.valueOf(el));
                    user.setEmail("mail@mail.com");
                    return user;
                })
                .collect(Collectors.toList());

        this.redisTemplate.opsForList().leftPushAll(USER_KEY+"-otherkey", users);
    }

    @Test
    public void getNumberOfAKey() {
        Long result = this.redisTemplate.opsForList().size(USER_KEY);
        System.out.println(result);
    }

    @Test
    public void getAtIndex() {
        System.out.println(this.redisTemplate.opsForList().index(OTHER_KEY,0 ));
        System.out.println(this.redisTemplate.opsForList().index(OTHER_KEY,1 ));
        System.out.println(this.redisTemplate.opsForList().index(OTHER_KEY,2 ));
        System.out.println(this.redisTemplate.opsForList().index(OTHER_KEY,3 ));
    }

    @Test
    public void removeWithObject() {
        User user = new User();
        user.setId(Long.valueOf(1L));
        user.setEmail("mail@mail.com");
        System.out.println(this.redisTemplate.opsForList().remove(OTHER_KEY,1 , user));
    }
}
