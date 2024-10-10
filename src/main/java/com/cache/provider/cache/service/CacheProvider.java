package com.cache.provider.cache.service;


public interface CacheProvider {

    boolean tryLock(String key, long waitTimeMillis, long leaseTimeMillis);

    void unlock(String key);

    void setString(String key, String value);

    String getString(String key);

    void put(String key, String field, String value);

    String get(String key, String field);

}
