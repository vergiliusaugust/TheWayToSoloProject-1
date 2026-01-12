package com.example.thewaytosoloproject1.exception;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException(Long id) {
        super("Task not found: id=" + id);
    }
}
