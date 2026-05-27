package ru.datamart.project.publishers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.ScadaDto;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerScada {
    @Value("${kafka.scada.topic}")
    private String scadaTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public boolean sendMessage(ScadaDto scadaDto) {
        try {
            String key = UUID.randomUUID().toString();
            String scadaDtoJson = objectMapper.writeValueAsString(scadaDto);
            kafkaTemplate.send(scadaTopic, key, scadaDtoJson);
        } catch (Exception e) {
            log.error("Произошла ошибка при сериализации dto в json", e);
            return false;
        }
        log.info("Отправил SCADA: " + scadaDto);
        return true;
    }
}