package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.datamart.project.models.Notification;
import ru.datamart.project.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;

    @GetMapping
    public List<Notification> getAll() {
        return service.getActive();
    }

    @PostMapping("/{id}/viewed")
    public void markViewed(@PathVariable Long id) {
        service.markAsViewed(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}