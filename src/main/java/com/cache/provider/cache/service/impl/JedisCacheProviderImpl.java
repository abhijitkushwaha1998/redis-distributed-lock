package com.cache.provider.cache.service.impl;


import com.cache.provider.cache.service.CacheProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@Primary
public class JedisCacheProviderImpl implements CacheProvider {

    @Autowired
    private Jedis jedis;

    @Override
    public boolean tryLock(final String key, final long waitTimeMillis, final long leaseTimeMillis) {
        long endTime = System.currentTimeMillis() + waitTimeMillis;
        SetParams params = new SetParams().nx().px(leaseTimeMillis);

        while (System.currentTimeMillis() < endTime) {
            String result = jedis.set(key, "LOCKED", params);
            if ("OK".equals(result)) {
                return true;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100); // Retry after a short delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    @Override
    public void unlock(final String key) {
        jedis.del(key);
    }

    // Implement string set/get logic
    @Override
    public void setString(final String key, final String value) {
        jedis.set("bucket:" + key, value); // Namespacing the key for strings
    }

    @Override
    public String getString(final String key) {
        return jedis.get("bucket:" + key);
    }

    @Override
    public void put(String key, String field, String value) {
        jedis.hset(key, field, value);
    }

    @Override
    public String get(String key, String field) {
        return jedis.hget(key, field);
    }

}
