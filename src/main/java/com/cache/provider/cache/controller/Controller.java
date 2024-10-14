package com.cache.provider.cache.controller;

import com.cache.provider.cache.service.CacheProvider;
import com.cache.provider.cache.service.impl.JedisCacheProviderImpl;
import com.cache.provider.cache.service.impl.RedissonCacheProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private JedisCacheProviderImpl cacheProvider;

    @GetMapping("/api/set")
    public String set(@RequestParam(name = "key") String key,
                      @RequestParam(name = "value") String value) throws InterruptedException {
        if (this.cacheProvider.tryLock(key, 40000, 60000000)) {
            this.cacheProvider.put("test", key, value);
            Thread.sleep(15000);
            this.cacheProvider.unlock(key);
        } else {
            return "Could not acquire the lock";
        }
        return "done";

    }

    @GetMapping("/api/set2")
    public String set2(@RequestParam(name = "key") String key,
                       @RequestParam(name = "value") String value) throws InterruptedException {
        if (this.cacheProvider.tryLock(key, 5000, 60000000)) {
            this.cacheProvider.put("test", key, value);
            Thread.sleep(5000);
            this.cacheProvider.unlock(key);
        } else {
            return "Could not acquire the lock";
        }
        return "done";

    }

    @GetMapping("/api/get")
    public String get(@RequestParam(name = "key") String key) {
        return this.cacheProvider.get("test", key);
    }

}