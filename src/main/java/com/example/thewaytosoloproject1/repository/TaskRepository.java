package com.example.thewaytosoloproject1.repository;

import com.example.thewaytosoloproject1.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.example.thewaytosoloproject1.model.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import com.example.thewaytosoloproject1.model.TaskStatus;



public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCategoryId(Long categoryId);

    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<Task> findByType(TaskType type);

    List<Task> findByUserIdAndType(Long userId, TaskType type);

    List<Task> findByCategoryIdAndType(Long categoryId, TaskType type);

    List<Task> findByUserIdAndCategoryIdAndType(Long userId, Long categoryId, TaskType type);

    Page<Task> findByUserIdAndCategoryIdAndType(Long userId, Long categoryId, TaskType type, Pageable pageable);
    Page<Task> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable);
    Page<Task> findByUserIdAndType(Long userId, TaskType type, Pageable pageable);
    Page<Task> findByCategoryIdAndType(Long categoryId, TaskType type, Pageable pageable);
    Page<Task> findByUserId(Long userId, Pageable pageable);
    Page<Task> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Task> findByType(TaskType type, Pageable pageable);
    Page<Task> findAll(Pageable pageable);

    @Query("""
    SELECT t FROM Task t
    WHERE (:userId IS NULL OR t.user.id = :userId)
      AND (:categoryId IS NULL OR t.category.id = :categoryId)
      AND (:type IS NULL OR t.type = :type)
      AND (:status IS NULL OR t.status = :status)
      AND (:createdFrom IS NULL OR t.createdAt >= :createdFrom)
      AND (:createdTo IS NULL OR t.createdAt <= :createdTo)
      AND (:dueFrom IS NULL OR t.dueDate >= :dueFrom)
      AND (:dueTo IS NULL OR t.dueDate <= :dueTo)
""")
    Page<Task> search(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("type") TaskType type,
            @Param("status") TaskStatus status,
            @Param("createdFrom") LocalDateTime createdFrom,
            @Param("createdTo") LocalDateTime createdTo,
            @Param("dueFrom") LocalDateTime dueFrom,
            @Param("dueTo") LocalDateTime dueTo,
            Pageable pageable
    );


}

