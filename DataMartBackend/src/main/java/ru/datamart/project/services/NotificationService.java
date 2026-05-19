package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.CustomEntityNotFoundException;
import ru.datamart.project.customExceptions.InvalidCredentialsException;
import ru.datamart.project.customExceptions.CustomInvalidRequestException;
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

    public List<NotificationEntity> getActive() {
        return notificationRepository.getActive();
    }

    public long countAlarmsByBatchId(String batchId) {
        if (batchId == null) {
            throw new CustomInvalidRequestException("Укажите ID партии.");
        }
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
        NotificationEntity saved;
        try {
            saved = notificationRepository.save(n);
        } catch (Exception e) {
            throw new InvalidCredentialsException("Не удалось создать уведомление.");
        }
        webSocketService.sendNotificationsUpdate(new SimpleWsMessageDto("NOTIFICATION_create"));
        log.info("СОЗДАНО УВЕДОМЛЕНИЕ:\n" + saved);
        return saved;
    }

    public void markAsViewed(Long id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID уведомления.");
        }
        NotificationEntity n = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException("Уведомление с таким ID не было найдено."));
        n.setViewed(true);
        notificationRepository.save(n);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID уведомления.");
        }
        notificationRepository.deleteById(id);
    }
}