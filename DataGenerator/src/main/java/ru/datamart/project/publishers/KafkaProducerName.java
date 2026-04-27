package ru.datamart.project.publishers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerName {
    @Value("${kafka.name.topic}")
    private String nameTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public boolean sendMessage(NameDto nameDto) {
        try {
            //todo вот тут выбрать ключ, по которому будет деление по партициям
            String key = UUID.randomUUID().toString();

            String nameDtoJson = objectMapper.writeValueAsString(nameDto);
            kafkaTemplate.send(nameTopic, key, nameDtoJson);
        } catch (Exception e) {
            log.error("Произошла ошибка при сериализации dto в json", e);
            return false;
        }
        return true;
    }
}