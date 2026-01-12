package com.example.thewaytosoloproject1.exception;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category not found: id=" + id);
    }
}
