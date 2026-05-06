package ru.datamart.project.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.dto.SimpleWsMessageDto;
import ru.datamart.project.models.MesEntity;
import ru.datamart.project.models.MesStatusEnum;
import ru.datamart.project.models.NotificationSeverityEnum;
import ru.datamart.project.services.MesService;
import ru.datamart.project.services.NotificationService;
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

    @KafkaListener(concurrency = "2", topics = "${kafka.mes.topic}", groupId = "${kafka.mes.group}")
    private void addMesRecord(String data) {
        try {
            MesDto mesDto = objectMapper.readValue(data, MesDto.class);
            Optional<MesEntity> mesEntityOptional = mesService.save(mesDto);
            if (mesEntityOptional.isPresent()) {
                MesEntity mes = mesEntityOptional.get();
                log.info(mes.toString());
                webSocketService.sendMesUpdate(new SimpleWsMessageDto("MES_update"));
                MesStatusEnum mesStatus = mes.getStatus();
                if (mesStatus != null && !mesStatus.equals(MesStatusEnum.NORMAL)) {
                    String message = "Показатели при обработке партии вышли за пределы нормы!";
                    NotificationSeverityEnum severity =
                            mesStatus.equals(MesStatusEnum.WARNING) ?
                                    NotificationSeverityEnum.WARNING : NotificationSeverityEnum.ALARM;
                    notificationService.create(message, severity,
                            mes.getEquipmentId(), "MES");
                }
            } else {
                log.info("MesEntity не создана. Ошибка в данных DTO");
            }
        } catch (Exception e) {
            log.error("Произошла ошибка", e);
        }
    }
}