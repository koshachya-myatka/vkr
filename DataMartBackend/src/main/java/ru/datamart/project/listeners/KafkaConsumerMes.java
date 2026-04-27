package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.services.MesService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerMes {
    private final MesService mesService;
    private final ObjectMapper objectMapper;

    @KafkaListener(concurrency = "2", topics = "${kafka.mes.topic}", groupId = "${kafka.mes.group}")
    private void addMesRecord(String data) {
        try {
            MesDto mesDto = objectMapper.readValue(data, MesDto.class);
            log.info(mesDto.toString());
//            Optional<MesRecord> mesRecordOptional = mesService.add(mesDto);
//            if (mesRecordOptional.isPresent()) {
//                log.info(mesRecordOptional.get());
//            } else {
//                log.info("MesRecord не создана. Ошибка в данных DTO");
//            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}