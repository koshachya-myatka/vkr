package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.datamart.project.dto.notifications.NotificationListFilterDto;
import ru.datamart.project.dto.notifications.NotificationListItemDto;
import ru.datamart.project.dto.notifications.NotificationStatsDto;
import ru.datamart.project.dto.notifications.NotificationUpdateDto;
import ru.datamart.project.dto.other.PageResponseDto;
import ru.datamart.project.models.NotificationEntity;
import ru.datamart.project.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/active")
    public ResponseEntity<List<NotificationEntity>> getAll() {
        return ResponseEntity.ok(notificationService.getActive());
    }

    @PostMapping("/{id}/in-progress")
    public ResponseEntity<?> markInProgress(@PathVariable Long id, Authentication authentication) {
        notificationService.markInProgress(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<PageResponseDto<NotificationListItemDto>> getNotifications(@RequestBody NotificationListFilterDto dto) {
        return ResponseEntity.ok(notificationService.getNotifications(dto));
    }

    @GetMapping("/stats")
    public ResponseEntity<NotificationStatsDto> getStats() {
        return ResponseEntity.ok(notificationService.getStats());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody NotificationUpdateDto dto,
                                    Authentication authentication) {
        notificationService.update(id, dto, authentication.getName());
        return ResponseEntity.ok().build();
    }
}