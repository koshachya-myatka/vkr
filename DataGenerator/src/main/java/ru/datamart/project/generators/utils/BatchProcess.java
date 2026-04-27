package ru.datamart.project.generators.utils;

import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.publishers.KafkaProducerLims;
import ru.datamart.project.publishers.KafkaProducerMes;
import ru.datamart.project.publishers.KafkaProducerScada;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class BatchProcess extends Thread {
    private final ObjectMapper objectMapper;
    private final KafkaProducerMes producerMes;
    private final KafkaProducerScada producerScada;
    private final KafkaProducerLims producerLims;

    private final String batchId = "BATCH-" + UUID.randomUUID();
    private final String equipmentId = "EQ-" + ThreadLocalRandom.current().nextInt(1, 20);
    private final String metalType = MetalType.random();
    private final AtomicBoolean processing = new AtomicBoolean(true);

    @Override
    public void run() {
        try {
            // 1. Создание партии
            MesDto mesDto = MesDtoGenerator.generate(batchId, equipmentId, metalType);

            //todo поправить время
            Thread.sleep(randomBetween(10000, 30000));

            // 2. Обработка
            objectMapper.updateValue(mesDto, MesDtoGenerator.startProcessing(mesDto));
            sendMesDto(mesDto);
            ScheduledExecutorService scadaExecutor = Executors.newSingleThreadScheduledExecutor();
            scadaExecutor.scheduleAtFixedRate(
                    this::sendScadaDto,
                    0,
                    randomBetween(1, 10),
                    TimeUnit.SECONDS
            );
            //todo поправить время
            Thread.sleep(randomBetween(30000, 90000));
            processing.compareAndSet(true, false);
            scadaExecutor.shutdownNow();

            // 3. Анализ
            mesDto.setProcessStatus(2);
            sendMesDto(mesDto);
            //todo поправить время
            Thread.sleep(randomBetween(60000, 90000));
            List<LimsDto> limsDtoList = LimsDtoGenerator.generate(batchId, metalType, randomBetween(1, 3));
            sendLimsDto(limsDtoList);

            // 4. Финальный статус
            //todo поправить?
            boolean defect = limsDtoList.stream().anyMatch(l -> l.getStatus().ordinal() == 2);
            mesDto.setProcessStatus(defect ? 4 : 3);
            sendMesDto(mesDto);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendMesDto(MesDto dto) {
        if (!producerMes.sendMessage(dto)) {
            //todo тут чето придумать?
            throw new RuntimeException("Не удалось отправить MesDto");
        }
    }

    private void sendScadaDto() {
        if (!processing.get()) {
            return;
        }
        ScadaDto dto = ScadaDtoGenerator.generate(equipmentId);
        if (!producerScada.sendMessage(dto)) {
            //todo тут чето придумать?
            throw new RuntimeException("Не удалось отправить ScadaDto");
        }
    }

    private void sendLimsDto(List<LimsDto> limsDtoList) {
        for (LimsDto dto : limsDtoList) {
            if (!producerLims.sendMessage(dto)) {
                //todo тут чето придумать?
                throw new RuntimeException("Не удалось отправить LimsDto");
            }
        }
    }

    private int randomBetween(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }
}