package ru.datamart.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginRequestDto {
    private String username;
    private String password;
}