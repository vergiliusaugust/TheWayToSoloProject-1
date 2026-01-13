package com.example.thewaytosoloproject1.cache;

import com.example.thewaytosoloproject1.dto.TaskResponse;
import com.example.thewaytosoloproject1.model.TaskType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "app.cache.redis", name = "enabled", havingValue = "false", matchIfMissing = true)
public class NoOpTaskCache implements TaskCache {

    @Override
    public List<TaskResponse> get(Long userId, Long categoryId, TaskType type) {
        return null;
    }

    @Override
    public void put(Long userId, Long categoryId, TaskType type, List<TaskResponse> value) { }

    @Override
    public void evictAll() { }
}
