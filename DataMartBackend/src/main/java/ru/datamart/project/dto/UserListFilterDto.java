package ru.datamart.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserListFilterDto {
    private Integer offset;
    private String username;
    private String name;
    private String surname;
    private String role;
}