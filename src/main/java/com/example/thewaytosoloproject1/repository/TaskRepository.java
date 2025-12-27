package com.example.thewaytosoloproject1.repository;

import com.example.thewaytosoloproject1.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCategoryId(Long categoryId);

    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndCategoryId(Long userId, Long categoryId);
}

