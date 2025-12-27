package com.example.thewaytosoloproject1.repository;

import com.example.thewaytosoloproject1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
