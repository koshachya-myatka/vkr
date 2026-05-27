package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.batchData.BatchScadaDto;
import ru.datamart.project.dto.kafkaData.ScadaDto;
import ru.datamart.project.dto.websocket.ScadaWsMessageDto;
import ru.datamart.project.models.NotificationSeverityEnum;
import ru.datamart.project.models.ScadaEntity;
import ru.datamart.project.models.ScadaStatusEnum;
import ru.datamart.project.services.NotificationService;
import ru.datamart.project.services.ProcessingBatchRegistry;
import ru.datamart.project.services.ScadaService;
import ru.datamart.project.services.WebSocketService;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerScada {
    private final ObjectMapper objectMapper;
    private final ScadaService scadaService;
    private final WebSocketService webSocketService;
    private final NotificationService notificationService;
    private final ProcessingBatchRegistry processingBatchRegistry;

    @KafkaListener(concurrency = "5", topics = "${kafka.scada.topic}", groupId = "${kafka.scada.group}")
    private void addScadaRecord(String data) {
        try {
            ScadaDto scadaDto = objectMapper.readValue(data, ScadaDto.class);
            Optional<ScadaEntity> scadaEntityOptional = scadaService.save(scadaDto);
            if (scadaEntityOptional.isPresent()) {
                ScadaEntity scada = scadaEntityOptional.get();
                log.info(scada.toString());

                Set<String> batchIds = processingBatchRegistry.getBatchIds(scada.getEquipmentId());
                for (String batchId : batchIds) {
                    BatchScadaDto scadaWsDto = objectMapper.convertValue(scada, BatchScadaDto.class);
                    scadaWsDto.setStatus(scada.getStatus().name());
                    webSocketService.sendScadaUpdate(new ScadaWsMessageDto(batchId, scadaWsDto));
                }

                ScadaStatusEnum scadaStatus = scada.getStatus();
                if (!scadaStatus.equals(ScadaStatusEnum.NORMAL)) {
                    String message = "Показатели прибора вышли за пределы нормы!";
                    NotificationSeverityEnum severity =
                            scadaStatus.equals(ScadaStatusEnum.WARNING) ?
                                    NotificationSeverityEnum.WARNING : NotificationSeverityEnum.ALARM;
                    notificationService.create(message, severity,
                            scada.getEquipmentId(), scada.getSensorId(), "SCADA");
                }
            } else {
                log.info("ScadaEntity не создана. Ошибка в данных DTO");
            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}