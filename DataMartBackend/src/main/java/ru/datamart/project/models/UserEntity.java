package ru.datamart.project.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}