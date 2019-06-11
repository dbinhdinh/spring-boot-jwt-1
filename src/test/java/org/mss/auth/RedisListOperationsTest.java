package org.mss.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * ListOperations is used for Redis list specific operations. Find some of its methods.
 * leftPush(K key, V value): Prepends value to key.
 * rightPush(K key, V value): Appends value to key.
 * leftPop(K key): Removes and returns first element in list stored at key.
 * rightPop(K key): Removes and returns last element in list stored at key.
 * remove(K key, long count, Object value): Removes the first given number (count) of occurrences of value from the list stored at key.
 * index(K key, long index): Fetches element at index from list at key.
 * size(K key): Fetches the size of list stored at key.
 *
 * Now find the example of ListOperations. Here we are performing create, read and delete operations
 */
public class RedisListOperationsTest extends RedisTest {

    @Resource(name="redisTemplate")
    private ListOperations<String, Object> opsForList;


    @Test
    public void getAtIndex() {
        System.out.println(this.opsForList.index(OTHER_KEY,0 ));
        System.out.println(this.opsForList.index(OTHER_KEY,1 ));
        System.out.println(this.opsForList.index(OTHER_KEY,2 ));
        System.out.println(this.opsForList.index(OTHER_KEY,3 ));
    }
}
