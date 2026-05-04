package ru.datamart.project.publishers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.MesDto;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerMes {
    @Value("${kafka.mes.topic}")
    private String mesTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public boolean sendMessage(MesDto mesDto) {
        try {
            //todo вот тут выбрать ключ, по которому будет деление по партициям
            String key = UUID.randomUUID().toString();

            String mesDtoJson = objectMapper.writeValueAsString(mesDto);
            kafkaTemplate.send(mesTopic, key, mesDtoJson);
        } catch (Exception e) {
            log.error("Произошла ошибка при сериализации dto в json", e);
            return false;
        }
        log.info("Отправил MES: " + mesDto);
        return true;
    }
}