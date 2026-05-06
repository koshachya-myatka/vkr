package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.SimpleWsMessageDto;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationsUpdate(SimpleWsMessageDto message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    public void sendMesUpdate(SimpleWsMessageDto message) {
        messagingTemplate.convertAndSend("/topic/mes", message);
    }

    public void sendLimsUpdate(SimpleWsMessageDto message) {
        messagingTemplate.convertAndSend("/topic/lims", message);
    }

    public void sendScadaUpdate(SimpleWsMessageDto message) {
        messagingTemplate.convertAndSend("/topic/scada", message);
    }
}