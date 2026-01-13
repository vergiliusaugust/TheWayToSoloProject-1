package com.example.thewaytosoloproject1.cache;

import com.example.thewaytosoloproject1.dto.TaskResponse;
import com.example.thewaytosoloproject1.model.TaskType;

import java.util.List;

public interface TaskCache {
    List<TaskResponse> get(Long userId, Long categoryId, TaskType type);
    void put(Long userId, Long categoryId, TaskType type, List<TaskResponse> value);
    void evictAll();
}
