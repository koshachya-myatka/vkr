package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.services.LimsService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerLims {
    private final LimsService limsService;
    private final ObjectMapper objectMapper;

    @KafkaListener(concurrency = "2", topics = "${kafka.lims.topic}", groupId = "${kafka.lims.group}")
    private void addLimsRecord(String data) {
        try {
            LimsDto limsDto = objectMapper.readValue(data, LimsDto.class);
            log.info(limsDto.toString());
//            Optional<LimsRecord> limsRecordOptional = limsService.add(limsDto);
//            if (limsRecordOptional.isPresent()) {
//                log.info(limsRecordOptional.get());
//            } else {
//                log.info("LimsRecord не создана. Ошибка в данных DTO");
//            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}