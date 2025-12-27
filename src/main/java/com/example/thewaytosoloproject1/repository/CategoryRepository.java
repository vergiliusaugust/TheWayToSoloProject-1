package com.example.thewaytosoloproject1.repository;

import com.example.thewaytosoloproject1.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
