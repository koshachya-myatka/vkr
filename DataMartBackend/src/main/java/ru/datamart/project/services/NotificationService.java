package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.SimpleWsMessageDto;
import ru.datamart.project.models.NotificationEntity;
import ru.datamart.project.models.NotificationSeverityEnum;
import ru.datamart.project.repositories.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final WebSocketService webSocketService;
    private final NotificationRepository notificationRepository;

    public long countAlarmsByBatchId(String batchId) {
        return notificationRepository.countAlarmsByBatchId(batchId);
    }

    public NotificationEntity create(String message, NotificationSeverityEnum severity,
                                     String equipmentId, String signalSource) {
        NotificationEntity n = new NotificationEntity();
        n.setViewed(false);
        n.setCreatedAt(LocalDateTime.now());
        n.setMessage(message);
        n.setEquipmentId(equipmentId);
        n.setSignalSource(signalSource);
        n.setSeverity(severity);
        NotificationEntity saved = notificationRepository.save(n);
        webSocketService.sendNotificationsUpdate(new SimpleWsMessageDto("NOTIFICATION_create"));
        log.info("СОЗДАНО УВЕДОМЛЕНИЕ:\n" + saved);
        return saved;
    }

    public List<NotificationEntity> getActive() {
        return notificationRepository.getLastNotifications();
    }

    public void markAsViewed(Long id) {
        NotificationEntity n = notificationRepository.findById(id).orElseThrow();
        n.setViewed(true);
        notificationRepository.save(n);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}