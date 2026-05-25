package ru.datamart.project.dto.users;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserListFilterDto {
    private Integer offset;
    private Integer limit;
    private String username;
    private String name;
    private String surname;
    private String role;
}