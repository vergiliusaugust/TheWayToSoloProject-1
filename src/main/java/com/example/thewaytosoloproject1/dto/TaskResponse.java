package com.example.thewaytosoloproject1.dto;

import lombok.Data;

import java.time.LocalDateTime;
import com.example.thewaytosoloproject1.model.TaskType;


@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private Long userId;
    private Long categoryId;
    private TaskType type;
}
