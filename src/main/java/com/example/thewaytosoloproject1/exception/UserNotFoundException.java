package com.example.thewaytosoloproject1.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User not found: id=" + id);
    }
}
