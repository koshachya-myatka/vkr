package ru.datamart.project.dto.auth;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthResponseDto {
    private String token;
    private String role;
}