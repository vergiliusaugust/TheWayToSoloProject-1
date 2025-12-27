package com.example.thewaytosoloproject1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime dueDate;
    private Long userId;
    private Long categoryId;
}





