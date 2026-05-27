package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.websocket.BatchIdWsMessageDto;
import ru.datamart.project.dto.websocket.ScadaWsMessageDto;
import ru.datamart.project.dto.websocket.SimpleWsMessageDto;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationsUpdate(SimpleWsMessageDto message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    public void sendMesUpdate(String batchId) {
        messagingTemplate.convertAndSend("/topic/mes", new BatchIdWsMessageDto("MES_UPDATE", batchId));
    }

    public void sendLimsUpdate(String batchId) {
        messagingTemplate.convertAndSend("/topic/lims", new BatchIdWsMessageDto("LIMS_UPDATE", batchId));
    }

    public void sendScadaUpdate(ScadaWsMessageDto dto) {
        messagingTemplate.convertAndSend("/topic/scada", dto);
    }
}