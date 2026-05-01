package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.MesDto;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMesUpdate(MesDto dto) {
        messagingTemplate.convertAndSend(
                "/topic/mes",
                dto
        );
    }
}