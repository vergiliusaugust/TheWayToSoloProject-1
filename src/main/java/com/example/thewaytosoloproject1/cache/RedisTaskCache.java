package com.example.thewaytosoloproject1.cache;

import com.example.thewaytosoloproject1.dto.TaskResponse;
import com.example.thewaytosoloproject1.model.TaskType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "app.cache.redis", name = "enabled", havingValue = "true")
public class RedisTaskCache implements TaskCache {

    private static final String CACHE_NAME = "tasks";
    private final CacheManager cacheManager;

    public RedisTaskCache(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private String key(Long userId, Long categoryId, TaskType type) {
        return "u=" + userId + ":c=" + categoryId + ":t=" + type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TaskResponse> get(Long userId, Long categoryId, TaskType type) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) return null;
        return cache.get(key(userId, categoryId, type), List.class);
    }

    @Override
    public void put(Long userId, Long categoryId, TaskType type, List<TaskResponse> value) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) cache.put(key(userId, categoryId, type), value);
    }

    @Override
    public void evictAll() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) cache.clear();
    }
}

