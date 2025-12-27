package com.example.thewaytosoloproject1.service;

import com.example.thewaytosoloproject1.dto.TaskRequest;
import com.example.thewaytosoloproject1.dto.TaskResponse;
import com.example.thewaytosoloproject1.exception.NotFoundException;
import com.example.thewaytosoloproject1.model.Category;
import com.example.thewaytosoloproject1.model.Task;
import com.example.thewaytosoloproject1.model.User;
import com.example.thewaytosoloproject1.repository.CategoryRepository;
import com.example.thewaytosoloproject1.repository.TaskRepository;
import com.example.thewaytosoloproject1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    public TaskService(TaskRepository taskRepo,
                       UserRepository userRepo,
                       CategoryRepository categoryRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    public TaskResponse create(TaskRequest req) {

        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new NotFoundException("User with id=" + req.getUserId() + " not found"));

        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category with id=" + req.getCategoryId() + " not found"));

        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .completed(req.isCompleted())
                .createdAt(LocalDateTime.now())    // подстраховка к @PrePersist
                .dueDate(req.getDueDate())
                .user(user)
                .category(category)
                .build();

        return toResponse(taskRepo.save(task));
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id=" + id + " not found"));
        return toResponse(task);
    }

    public List<TaskResponse> getAll() {
        return taskRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TaskResponse> getByCategory(Long categoryId) {
        return taskRepo.findByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TaskResponse> getByUser(Long userId) {
        return taskRepo.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TaskResponse> getByUserAndCategory(Long userId, Long categoryId) {
        return taskRepo.findByUserId(userId).stream()
                .filter(task -> task.getCategory().getId().equals(categoryId))
                .map(this::toResponse)
                .toList();
    }

    // НОВЫЙ МЕТОД — список задач по списку id
    public List<TaskResponse> getByIds(List<Long> ids) {
        return taskRepo.findAllById(ids).stream()
                .map(this::toResponse)
                .toList();
    }

    public TaskResponse update(Long id, TaskRequest req) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id=" + id + " not found"));

        if (req.getTitle() != null) {
            task.setTitle(req.getTitle());
        }
        if (req.getDescription() != null) {
            task.setDescription(req.getDescription());
        }

        task.setCompleted(req.isCompleted());
        task.setDueDate(req.getDueDate());

        if (req.getUserId() != null) {
            User user = userRepo.findById(req.getUserId())
                    .orElseThrow(() -> new NotFoundException("User with id=" + req.getUserId() + " not found"));
            task.setUser(user);
        }

        if (req.getCategoryId() != null) {
            Category category = categoryRepo.findById(req.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category with id=" + req.getCategoryId() + " not found"));
            task.setCategory(category);
        }

        return toResponse(taskRepo.save(task));
    }

    public void delete(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new NotFoundException("Task with id=" + id + " not found");
        }
        taskRepo.deleteById(id);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse dto = new TaskResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.isCompleted());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setDueDate(task.getDueDate());
        dto.setUserId(task.getUser().getId());
        dto.setCategoryId(task.getCategory().getId());
        return dto;
    }
}
