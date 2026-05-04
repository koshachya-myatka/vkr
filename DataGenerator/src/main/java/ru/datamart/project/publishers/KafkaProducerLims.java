package ru.datamart.project.publishers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.LimsDto;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerLims {
    @Value("${kafka.lims.topic}")
    private String limsTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public boolean sendMessage(LimsDto limsDto) {
        try {
            //todo вот тут выбрать ключ, по которому будет деление по партициям
            String key = UUID.randomUUID().toString();

            String limsDtoJson = objectMapper.writeValueAsString(limsDto);
            kafkaTemplate.send(limsTopic, key, limsDtoJson);
        } catch (Exception e) {
            log.error("Произошла ошибка при сериализации dto в json", e);
            return false;
        }
        log.info("Отправил LIMS: " + limsDto);
        return true;
    }
}