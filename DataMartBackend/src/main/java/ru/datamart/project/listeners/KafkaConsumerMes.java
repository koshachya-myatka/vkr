package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.kafkaData.MesDto;
import ru.datamart.project.models.MesEntity;
import ru.datamart.project.models.MesProcessStatusEnum;
import ru.datamart.project.models.NotificationSeverityEnum;
import ru.datamart.project.services.MesService;
import ru.datamart.project.services.NotificationService;
import ru.datamart.project.services.ProcessingBatchRegistry;
import ru.datamart.project.services.WebSocketService;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerMes {
    private final ObjectMapper objectMapper;
    private final MesService mesService;
    private final WebSocketService webSocketService;
    private final NotificationService notificationService;
    private final ProcessingBatchRegistry processingBatchRegistry;

    @KafkaListener(concurrency = "2", topics = "${kafka.mes.topic}", groupId = "${kafka.mes.group}")
    private void addMesRecord(String data) {
        try {
            MesDto mesDto = objectMapper.readValue(data, MesDto.class);
            Optional<MesEntity> mesEntityOptional = mesService.save(mesDto);
            if (mesEntityOptional.isPresent()) {
                MesEntity mes = mesEntityOptional.get();
                log.info(mes.toString());
                webSocketService.sendMesUpdate(mes.getBatch().getBatchId());

                if (mesDto.getProcessStatus().equals(MesProcessStatusEnum.PROCESSING)) {
                    processingBatchRegistry.addBatch(
                            mes.getEquipmentId(),
                            mes.getBatch().getBatchId()
                    );
                }

                if (mesDto.getProcessStatus().equals(MesProcessStatusEnum.ANALYSIS)) {
                    processingBatchRegistry.removeBatch(
                            mes.getEquipmentId(),
                            mes.getBatch().getBatchId()
                    );
                }

            } else {
                log.info("MesEntity не создана. Ошибка в данных DTO");
            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}