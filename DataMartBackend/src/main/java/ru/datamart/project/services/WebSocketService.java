package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.models.Notification;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationUpdate(Notification notification) {
        messagingTemplate.convertAndSend(
                "/topic/notifications",
                notification
        );
    }

    public void sendMesUpdate(MesDto dto) {
        messagingTemplate.convertAndSend(
                "/topic/mes",
                dto
        );
    }
}