package com.example.thewaytosoloproject1.controller;

import com.example.thewaytosoloproject1.dto.TaskRequest;
import com.example.thewaytosoloproject1.dto.TaskResponse;
import com.example.thewaytosoloproject1.model.TaskType;
import com.example.thewaytosoloproject1.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;
    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponse>
    create(@RequestBody TaskRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping("/{id}")
    public TaskResponse
    getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Page<TaskResponse> getTasks(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) TaskType type,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) LocalDateTime createdFrom,
            @RequestParam(required = false) LocalDateTime createdTo,
            @RequestParam(required = false) LocalDateTime dueFrom,
            @RequestParam(required = false) LocalDateTime dueTo,
            Pageable pageable
    ) {
        return service.getTasks(userId, categoryId, type, status, createdFrom, createdTo, dueFrom, dueTo, pageable);
    }


    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @RequestBody TaskRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping("/by-ids")
    public List<TaskResponse> getByIds(@RequestParam("ids") List<Long> ids) {
        return service.getByIds(ids);
    }
}
