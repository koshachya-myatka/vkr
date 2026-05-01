package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.models.MesEntity;
import ru.datamart.project.services.MesService;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerMes {
    private final MesService mesService;
    private final ObjectMapper objectMapper;

    @KafkaListener(concurrency = "5", topics = "${kafka.mes.topic}", groupId = "${kafka.mes.group}")
    private void addMesRecord(String data) {
        try {
            MesDto mesDto = objectMapper.readValue(data, MesDto.class);
            Optional<MesEntity> mesEntityOptional = mesService.save(mesDto);
            if (mesEntityOptional.isPresent()) {
                log.info(mesEntityOptional.get().toString());
            } else {
                log.info("MesEntity не создана. Ошибка в данных DTO");
            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}