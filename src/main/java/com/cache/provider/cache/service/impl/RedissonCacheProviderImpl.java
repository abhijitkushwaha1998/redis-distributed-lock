package com.cache.provider.cache.service.impl;


import com.cache.provider.cache.service.CacheProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class RedissonCacheProviderImpl implements CacheProvider {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean tryLock(final String key, final long waitTimeMillis, final long leaseTimeMillis) {
        val lock = this.redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTimeMillis, leaseTimeMillis, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void unlock(final String key) {
        val lock = this.redissonClient.getLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    public void setString(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    @Override
    public String getString(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public void put(String key, String field, String value) {
        // Get the RMap instance for the given key
        RMap<String, String> map = redissonClient.getMapCache(key);
        // Set the field and value in the hash
        map.put(field, value);
    }

    @Override
    public String get(String key, String field) {
        // Get the RMap instance for the given key
        RMap<String, String> map = redissonClient.getMapCache(key);
        // Return the value associated with the field in the hash
        return map.get(field);
    }

}
