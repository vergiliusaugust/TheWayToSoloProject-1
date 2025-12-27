package com.example.thewaytosoloproject1.service;

import com.example.thewaytosoloproject1.dto.TaskRequest;
import com.example.thewaytosoloproject1.dto.UserRequest;
import com.example.thewaytosoloproject1.dto.UserResponse;
import com.example.thewaytosoloproject1.exception.NotFoundException;
import com.example.thewaytosoloproject1.model.User;
import com.example.thewaytosoloproject1.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserResponse create(UserRequest req) {
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .build();

        return toResponse(repo.save(user));
    }

    public List<UserResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getById(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
        return toResponse(user);
    }

    public UserResponse update(Long id, UserRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));

        if (req.getUsername() != null) {
            user.setUsername(req.getUsername());
        }
        if (req.getEmail() != null) {
            user.setEmail(req.getEmail());
        }

        return toResponse(repo.save(user));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
        repo.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
