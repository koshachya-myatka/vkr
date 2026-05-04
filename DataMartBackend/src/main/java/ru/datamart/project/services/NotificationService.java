package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.datamart.project.models.Notification;
import ru.datamart.project.models.NotificationSeverityEnum;
import ru.datamart.project.repositories.NotificationRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final WebSocketService webSocketService;

    public Notification create(String message, NotificationSeverityEnum severity) {
        Notification n = new Notification();
        n.setMessage(message);
        n.setSeverity(severity);
        Notification saved = repository.save(n);
        webSocketService.sendNotificationUpdate(saved);
        return saved;
    }

    public List<Notification> getActive() {
        return repository.getLastNotifications();
    }

    public void markAsViewed(Long id) {
        Notification n = repository.findById(id).orElseThrow();
        n.setViewed(true);
        repository.save(n);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}