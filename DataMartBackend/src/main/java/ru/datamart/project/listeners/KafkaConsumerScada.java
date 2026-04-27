package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.services.ScadaService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerScada {
    private final ScadaService scadaService;
    private final ObjectMapper objectMapper;

    @KafkaListener(concurrency = "2", topics = "${kafka.scada.topic}", groupId = "${kafka.scada.group}")
    private void addScadaRecord(String data) {
        try {
            ScadaDto scadaDto = objectMapper.readValue(data, ScadaDto.class);
            Optional<ScadaRecord> scadaRecordOptional = scadaService.add(scadaDto);
            if (scadaRecordOptional.isPresent()) {
                log.info(scadaRecordOptional.get());
            } else {
                log.info("ScadaRecord не создана. Ошибка в данных DTO");
            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}