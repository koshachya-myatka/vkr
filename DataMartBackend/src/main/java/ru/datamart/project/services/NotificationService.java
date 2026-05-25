package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.CustomEntityNotFoundException;
import ru.datamart.project.customExceptions.InvalidCredentialsException;
import ru.datamart.project.customExceptions.CustomInvalidRequestException;
import ru.datamart.project.dto.notifications.NotificationListFilterDto;
import ru.datamart.project.dto.notifications.NotificationListItemDto;
import ru.datamart.project.dto.notifications.NotificationStatsDto;
import ru.datamart.project.dto.notifications.NotificationUpdateDto;
import ru.datamart.project.dto.other.PageResponseDto;
import ru.datamart.project.dto.websocket.SimpleWsMessageDto;
import ru.datamart.project.models.NotificationEntity;
import ru.datamart.project.models.NotificationSeverityEnum;
import ru.datamart.project.models.NotificationStatusEnum;
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

    public PageResponseDto<NotificationListItemDto> getNotifications(NotificationListFilterDto dto) {
        if (dto.getOffset() == null || dto.getLimit() == null || dto.getLimit().equals(0)) {
            throw new CustomInvalidRequestException("Кол-во искомых данных не определено.");
        }
        int limit = dto.getLimit();
        List<NotificationListItemDto> items = notificationRepository.getNotifications(
                dto.getOffset(),
                limit,
                dto.getEquipmentId(),
                dto.getSignalSource(),
                dto.getStatus(),
                dto.getDateFrom(),
                dto.getDateTo()
        );
        long totalItems = notificationRepository.countNotifications(
                dto.getEquipmentId(),
                dto.getSignalSource(),
                dto.getStatus(),
                dto.getDateFrom(),
                dto.getDateTo()
        );
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int currentPage = dto.getOffset() / limit + 1;
        return new PageResponseDto<>(items, totalItems, totalPages, currentPage);
    }

    public NotificationStatsDto getStats() {
        return notificationRepository.getStats();
    }

    public void markInProgress(Long id, String username) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID уведомления.");
        }
        NotificationEntity n = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException("Уведомление с таким ID не было найдено."));
        n.setViewed(true);
        n.setStatus(NotificationStatusEnum.IN_PROGRESS);
        n.setUpdatedBy(username);
        n.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(n);
    }

    public NotificationEntity create(String message, NotificationSeverityEnum severity,
                                     String equipmentId, String sensorId, String signalSource) {
        NotificationEntity n = new NotificationEntity();
        n.setViewed(false);
        n.setCreatedAt(LocalDateTime.now());
        n.setStatus(NotificationStatusEnum.CREATED);
        n.setMessage(message);
        n.setEquipmentId(equipmentId);
        n.setSensorId(sensorId);
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

    public void update(Long id, NotificationUpdateDto dto, String username) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID уведомления.");
        }
        NotificationEntity n = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomEntityNotFoundException("Уведомление не найдено."));
        n.setStatus(NotificationStatusEnum.valueOf(dto.getStatus()));
        n.setComment(dto.getComment());
        n.setUpdatedAt(LocalDateTime.now());
        n.setUpdatedBy(username);
        NotificationEntity saved;
        try {
            saved = notificationRepository.save(n);
        } catch (Exception e) {
            throw new InvalidCredentialsException("Не удалось изменить уведомление.");
        }
        log.info("ОБНОВЛЕНО УВЕДОМЛЕНИЕ:\n" + saved);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID уведомления.");
        }
        notificationRepository.deleteById(id);
    }
}