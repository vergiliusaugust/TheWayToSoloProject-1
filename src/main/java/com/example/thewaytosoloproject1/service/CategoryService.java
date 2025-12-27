package com.example.thewaytosoloproject1.service;

import com.example.thewaytosoloproject1.dto.CategoryRequest;
import com.example.thewaytosoloproject1.dto.CategoryResponse;
import com.example.thewaytosoloproject1.dto.TaskRequest;
import com.example.thewaytosoloproject1.exception.NotFoundException;
import com.example.thewaytosoloproject1.model.Category;
import com.example.thewaytosoloproject1.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public CategoryResponse create(CategoryRequest req) {
        Category category = Category.builder()
                .name(req.getName())
                .build();

        return toResponse(repo.save(category));
    }

    public List<CategoryResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getById(Long id) {
        Category category = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " not found"));
        return toResponse(category);
    }

    public CategoryResponse update(Long id, CategoryRequest req) {
        Category category = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " not found"));

        if (req.getName() != null) {
            category.setName(req.getName());
        }

        return toResponse(repo.save(category));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Category with id=" + id + " not found");
        }
        repo.deleteById(id);
    }

    private CategoryResponse toResponse(Category category) {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
