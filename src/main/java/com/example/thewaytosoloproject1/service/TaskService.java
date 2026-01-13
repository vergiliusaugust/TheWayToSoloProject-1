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
import com.example.thewaytosoloproject1.exception.CategoryNotFoundException;
import com.example.thewaytosoloproject1.exception.TaskNotFoundException;
import com.example.thewaytosoloproject1.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import com.example.thewaytosoloproject1.model.TaskType;
import com.example.thewaytosoloproject1.cache.TaskCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.thewaytosoloproject1.model.TaskStatus;


@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final TaskCache taskCache;


    public TaskService(TaskRepository taskRepo,
                       UserRepository userRepo,
                       CategoryRepository categoryRepo,
                       TaskCache taskCache) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.taskCache = taskCache;
    }


    public TaskResponse create(TaskRequest req) {

        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new NotFoundException("User with id=" + req.getUserId() + " not found"));

        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category with id=" + req.getCategoryId() + " not found"));

        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .type(req.getType() == null ? TaskType.CHORE : req.getType())
                .completed(req.isCompleted())
                .createdAt(LocalDateTime.now())    // подстраховка к @PrePersist
                .dueDate(req.getDueDate())
                .user(user)
                .category(category)
                .build();

        task.setCreatedAt(LocalDateTime.now());
        if (task.getStatus() == null) task.setStatus(TaskStatus.NEW);

        Task saved = taskRepo.save(task);

        taskCache.evictAll();
        return toResponse(taskRepo.save(task));
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
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
        if (req.getType() != null) {
            task.setType(req.getType());
        }


        task.setCompleted(req.isCompleted());
        task.setDueDate(req.getDueDate());

        if (req.getUserId() != null) {
            User user = userRepo.findById(req.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(req.getUserId()));
            task.setUser(user);
        }

        if (req.getCategoryId() != null) {
            Category category = categoryRepo.findById(req.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(req.getCategoryId()));
            task.setCategory(category);
        }
        Task saved = taskRepo.save(task);

        taskCache.evictAll();
        return toResponse(taskRepo.save(task));
    }

    public void delete(Long id) {
        if (!taskRepo.existsById(id)) {
            throw new NotFoundException("Task with id=" + id + " not found");
        }
        taskRepo.deleteById(id);

        Task task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskRepo.delete(task);

        taskCache.evictAll();
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse dto = new TaskResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setType(task.getType());
        dto.setCompleted(task.isCompleted());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setDueDate(task.getDueDate());
        dto.setUserId(task.getUser().getId());
        dto.setCategoryId(task.getCategory().getId());
        return dto;
    }


    public List<TaskResponse> getTasks(Long userId, Long categoryId, TaskType type) {

        List<Task> tasks;
        List<TaskResponse> cached = taskCache.get(userId, categoryId, type);
        if (cached != null) {
            return cached;
        }

        if (userId != null && categoryId != null && type != null) {
            tasks = taskRepo.findByUserIdAndCategoryIdAndType(userId, categoryId, type);
        } else if (userId != null && categoryId != null) {
            tasks = taskRepo.findByUserIdAndCategoryId(userId, categoryId);
        } else if (userId != null && type != null) {
            tasks = taskRepo.findByUserIdAndType(userId, type);
        } else if (categoryId != null && type != null) {
            tasks = taskRepo.findByCategoryIdAndType(categoryId, type);
        } else if (userId != null) {
            tasks = taskRepo.findByUserId(userId);
        } else if (categoryId != null) {
            tasks = taskRepo.findByCategoryId(categoryId);
        } else if (type != null) {
            tasks = taskRepo.findByType(type);
        } else {
            tasks = taskRepo.findAll();
        }

        List<TaskResponse> result = tasks.stream()
                .map(this::toResponse)
                .toList();

        taskCache.put(userId, categoryId, type, result);
        return result;

    }

    public Page<TaskResponse> getTasks(
            Long userId,
            Long categoryId,
            TaskType type,
            TaskStatus status,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            LocalDateTime dueFrom,
            LocalDateTime dueTo,
            Pageable pageable
    ) {
        Page<Task> page = taskRepo.search(
                userId,
                categoryId,
                type,
                status,
                createdFrom,
                createdTo,
                dueFrom,
                dueTo,
                pageable
        );

        return page.map(this::toResponse);
    }


}

