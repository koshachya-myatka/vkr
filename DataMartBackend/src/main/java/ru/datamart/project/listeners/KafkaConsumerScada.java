package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.models.ScadaEntity;
import ru.datamart.project.services.ScadaService;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerScada {
    private final ScadaService scadaService;
    private final ObjectMapper objectMapper;

    @KafkaListener(concurrency = "5", topics = "${kafka.scada.topic}", groupId = "${kafka.scada.group}")
    private void addScadaRecord(String data) {
        try {
            ScadaDto scadaDto = objectMapper.readValue(data, ScadaDto.class);
            Optional<ScadaEntity> scadaEntityOptional = scadaService.save(scadaDto);
            if (scadaEntityOptional.isPresent()) {
                log.info(scadaEntityOptional.get().toString());
            } else {
                log.info("ScadaEntity не создана. Ошибка в данных DTO");
            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}