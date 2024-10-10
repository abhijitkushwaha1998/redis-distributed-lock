package com.cache.provider.cache.controller;

import com.cache.provider.cache.service.CacheProvider;
import com.cache.provider.cache.service.impl.RedissonForRedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private RedissonForRedis cacheProvider;

    @GetMapping("/api/set")
    public void set(@RequestParam(name = "key") String key,
                    @RequestParam(name = "value") String value) {
        if (this.cacheProvider.tryLock(key, 40, 50)) {
            this.cacheProvider.put("test", key, value);
            this.cacheProvider.unlock(key);
        } else {
            log.info("Could not acquire the lock");
        }


    }

    @GetMapping("/api/get")
    public String get(@RequestParam(name = "key") String key) {
        return this.cacheProvider.get("test", key);
    }

}