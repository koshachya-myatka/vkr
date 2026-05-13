package ru.datamart.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserProfileDto {
    private String username;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String role;
}