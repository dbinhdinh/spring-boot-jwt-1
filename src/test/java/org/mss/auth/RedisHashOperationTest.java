package org.mss.auth;

import org.junit.Before;
import org.junit.Test;
import org.mss.auth.model.User;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;

/**
 * HashOperations
 * HashOperations performs Redis map specific operations working on a hash. Find some of its methods.
 * putIfAbsent(H key, HK hashKey, HV value): Sets the value of a hash hashKey only if hashKey does not exist.
 * put(H key, HK hashKey, HV value): Sets the value of a hash hashKey.
 * get(H key, Object hashKey): Fetches value for given hashKey from hash at key.
 * size(H key): Fetches size of hash at key.
 * entries(H key): Fetches entire hash stored at key.
 * delete(H key, Object... hashKeys): Deletes given hash hashKeys at key.
 */
public class RedisHashOperationTest extends RedisTest {
    private static final String KEY = "employeesKey";

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, User> hashOps;
    private User user;

    @Before
    public void init() {
        user = new User();
        user.setId(Long.valueOf(10L));
        user.setEmail("mail@mail.com");
    }

    public void addEmployee() {
        hashOps.putIfAbsent(KEY, user.getId(), user);
    }

    public void updateEmployee() {
        hashOps.put(KEY, user.getId(), user);
    }

    @Test
    public void getEmployee(Integer id) {
        System.out.println(hashOps.get(KEY, id));
    }

    public long getNumberOfEmployees() {
        System.out.println(hashOps.size(KEY));
    }

    public void getAllEmployees() {
        hashOps.entries(KEY).forEach((k, v) -> {
            System.out.println("key: " + k);
            System.out.println("val" + v.toString());
        });

    }

    public long deleteEmployees(Integer... ids) {
        return hashOps.delete(KEY, (Object) ids);
    }
}
