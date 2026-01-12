package com.example.thewaytosoloproject1.repository;

import com.example.thewaytosoloproject1.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.example.thewaytosoloproject1.model.TaskType;


public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCategoryId(Long categoryId);

    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<Task> findByType(TaskType type);

    List<Task> findByUserIdAndType(Long userId, TaskType type);

    List<Task> findByCategoryIdAndType(Long categoryId, TaskType type);

    List<Task> findByUserIdAndCategoryIdAndType(Long userId, Long categoryId, TaskType type);

}

