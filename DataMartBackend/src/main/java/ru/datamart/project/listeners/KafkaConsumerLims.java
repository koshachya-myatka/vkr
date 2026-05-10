package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.SimpleWsMessageDto;
import ru.datamart.project.models.LimsEntity;
import ru.datamart.project.services.LimsService;
import ru.datamart.project.services.WebSocketService;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerLims {
    private final ObjectMapper objectMapper;
    private final LimsService limsService;
    private final WebSocketService webSocketService;

    @KafkaListener(concurrency = "2", topics = "${kafka.lims.topic}", groupId = "${kafka.lims.group}")
    private void addLimsRecord(String data) {
        try {
            LimsDto limsDto = objectMapper.readValue(data, LimsDto.class);
            Optional<LimsEntity> limsEntityOptional = limsService.save(limsDto);
            if (limsEntityOptional.isPresent()) {
                LimsEntity lims = limsEntityOptional.get();
                log.info(lims.toString());
                webSocketService.sendLimsUpdate(lims.getBatch().getBatchId());
            } else {
                log.info("LimsEntity не создана. Ошибка в данных DTO");
            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}