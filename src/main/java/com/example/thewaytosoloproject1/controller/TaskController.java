package com.example.thewaytosoloproject1.controller;

import com.example.thewaytosoloproject1.dto.TaskRequest;
import com.example.thewaytosoloproject1.dto.TaskResponse;
import com.example.thewaytosoloproject1.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public List<TaskResponse>
    getAll( @RequestParam(required = false)
            Long categoryId,
            @RequestParam(required = false)
            Long userId ) {
        if (userId != null && categoryId != null) {
            return service.getByUserAndCategory(userId, categoryId);
        }
        else if (userId != null) {
            return service.getByUser(userId);
        }
        else if (categoryId != null) {
            return service.getByCategory(categoryId);
        }
        return service.getAll();
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