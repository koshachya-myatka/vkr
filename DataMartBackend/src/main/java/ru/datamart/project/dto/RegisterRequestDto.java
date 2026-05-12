package ru.datamart.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegisterRequestDto {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String role;
}