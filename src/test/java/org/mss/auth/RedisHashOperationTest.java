package org.mss.auth;

import org.junit.Before;
import org.junit.Test;
import org.mss.auth.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
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
public class RedisHashOperationTest<serializer> extends RedisTest {
    private static final String KEY = "employeesKey";

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, User> hashOps;
    private User user;


    private Converter<Object, byte[]> serializer;
    private Converter<byte[], Object> deserializer;

    @Before
    public void init() {
        serializer = new SerializingConverter();
        deserializer = new DeserializingConverter();

        user = new User();
        user.setId(Long.valueOf(10L));
        user.setEmail("mail@mail.com");
    }

    @Test
    public void addEmployee() {
        hashOps.putIfAbsent(KEY, user.getId(), user);
    }

    @Test
    public void updateEmployee() {
        hashOps.put(KEY, user.getId(), user);
    }

    @Test
    public void getEmployee(Integer id) {
        System.out.println(hashOps.get(KEY, id));
    }

    @Test
    public void getNumberOfEmployees() {
        System.out.println(hashOps.size(KEY));
    }

    @Test
    public void getAllEmployees() {
        hashOps.entries(KEY).forEach((k, v) -> {
            System.out.println("key: " + k);
            System.out.println("val" + v.toString());
        });

    }

    @Test
    public long deleteEmployees(Integer... ids) {
        return hashOps.delete(KEY, (Object) ids);
    }
}
