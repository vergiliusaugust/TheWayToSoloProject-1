package com.example.thewaytosoloproject1.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Можно будет расширить (password, roles и т.д.), пока минимально
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    // Обратная связь с задачами
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;
}
