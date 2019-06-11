package org.mss.auth;

import org.junit.Before;
import org.junit.Test;
import org.mss.auth.model.User;
import org.springframework.data.redis.core.SetOperations;

import javax.annotation.Resource;
import java.util.Set;

/**
 * SetOperations performs Redis set specific operations. Find some of its methods.
 * add(K key, V... values): Adds values to set at key.
 * members(K key): Fetches all elements of set at key.
 * size(K key): Fetches size of set at key.
 * remove(K key, Object... values): Removes given values from set at key and returns the number of removed elements.
 */
public class RedisSetOperationsTest extends RedisTest {
    public static final String KEY = "user-setOpr";
    private User user;
    @Resource(name = "redisTemplate")
    private SetOperations<String, Object> setOps;

    @Before
    public void init() {
        user = new User();
        user.setId(Long.valueOf(10L));
        user.setEmail("mail@mail.com");
    }

    @Test
    public void addFamilyMembers() {
        User user = new User();
        user.setId(Long.valueOf(-1L));
        user.setEmail("mail@mail.com");
        setOps.add(KEY, user);
    }

    @Test
    public void getFamilyMembers() {
        Set<Object> data = setOps.members(KEY);
        System.out.println(data);
    }

    @Test
    public void getNumberOfFamilyMembers() {
        Long data = setOps.size(KEY);
        System.out.println(data);
    }

    @Test
    public void removeFamilyMembers() {

//        return setOps.remove(KEY, (Object[]));
    }
}
