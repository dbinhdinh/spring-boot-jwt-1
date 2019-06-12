package org.mss.auth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mss.auth.model.Role;
import org.mss.auth.model.User;
import org.mss.auth.service.RedisJwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RedisValueOperationTest extends RedisTest {
    @Autowired
    private RedisJwtTokenService redisJwtTokenService;
    private Role role1;
    private Role role2;
    private Set<Role> roles;

    @Autowired
    private RedisTemplate redisTemplate;

    private Converter<Object, byte[]> serializer;
    private Converter<byte[], Object> deserializer;

    @Before
    public void init() {
        serializer = new SerializingConverter();
        deserializer = new DeserializingConverter();

        role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");

        role2 = new Role();
        role2.setId(2L);
        role2.setName("USER");


        roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
    }

    @Test
    public void saveToken() {
        List<User> data = IntStream.rangeClosed(0, 5).mapToObj(el -> {
            User user = new User();
            user.setEmail("email@gmail.com" + UUID.randomUUID().toString());
            user.setId(Long.valueOf(el));
            user.setRoles(roles);
            return user;
        }).collect(Collectors.toList());

        List<String> tokenKeys = new ArrayList<>();
        for (User user : data) {
            String tokenKey = UUID.randomUUID().toString();
            this.redisTemplate.opsForValue().set(tokenKey, user);
            tokenKeys.add(tokenKey);
        }

        tokenKeys.stream().forEach(System.out::println);

    }

    @Test
    public void setExpire() {
        Boolean res = this.redisTemplate.expire("30a1a95e-2883-4898-9485-e52ef99c58fe", 10, TimeUnit.SECONDS);
        System.out.println(res);
//        System.out.println(this.redisTemplate.opsForValue().get("30a1a95e-2883-4898-9485-e52ef99c58fe"));
    }

    @After
    public void clean() {
        role1 = null;
        role2 = null;
        roles = null;
    }
}
