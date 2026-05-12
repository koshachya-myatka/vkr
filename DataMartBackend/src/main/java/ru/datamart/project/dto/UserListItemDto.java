package ru.datamart.project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserListItemDto {
    private UUID userId;
    private String username;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
