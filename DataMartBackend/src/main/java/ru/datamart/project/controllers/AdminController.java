package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.datamart.project.dto.other.PageResponseDto;
import ru.datamart.project.dto.users.UserListFilterDto;
import ru.datamart.project.dto.users.UserListItemDto;
import ru.datamart.project.dto.users.UserUpdateDto;
import ru.datamart.project.services.AdminService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/users")
    public ResponseEntity<PageResponseDto<UserListItemDto>> getUsers(@RequestBody UserListFilterDto dto) {
        return ResponseEntity.ok(adminService.getUsers(dto));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateDto dto) {
        adminService.updateUser(userId, dto);
        return ResponseEntity.ok().build();
    }
}