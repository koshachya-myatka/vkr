package ru.datamart.project.generators.utils;

import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.models.LimsStatusEnum;
import ru.datamart.project.models.MesProcessStatusEnum;
import ru.datamart.project.models.MesStatusEnum;
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

public class BatchProcess implements Runnable {
    private final ObjectMapper objectMapper;
    private final KafkaProducerMes producerMes;
    private final KafkaProducerScada producerScada;
    private final KafkaProducerLims producerLims;
    private final Runnable onCompleted;

    private final boolean isDefective = ThreadLocalRandom.current().nextDouble() < 0.05;
    private final String batchId = "BATCH-" + UUID.randomUUID();
    private final String equipmentId = "EQ-" + ThreadLocalRandom.current().nextInt(1, 20);
    private final String metalType = MetalType.random();
    private final AtomicBoolean processing = new AtomicBoolean(true);

    public BatchProcess(ObjectMapper objectMapper, KafkaProducerMes producerMes,
                        KafkaProducerScada producerScada, KafkaProducerLims producerLims,
                        Runnable onCompleted) {
        this.objectMapper = objectMapper;
        this.producerMes = producerMes;
        this.producerScada = producerScada;
        this.producerLims = producerLims;
        this.onCompleted = onCompleted;
    }

    @Override
    public void run() {
        try {
            // 1. Создание партии
            MesDto mesDto = MesDtoGenerator.generate(batchId, equipmentId, metalType);
            sendMesDto(mesDto);
            Thread.sleep(randomBetween(10000, 20000));

            // 2. Обработка
            objectMapper.updateValue(mesDto, MesDtoGenerator.startProcessing(mesDto));
            sendMesDto(mesDto);
            ScheduledExecutorService scadaExecutor = Executors.newSingleThreadScheduledExecutor();
            scadaExecutor.scheduleAtFixedRate(
                    this::sendScadaDto,
                    0,
                    randomBetween(1, 5),
                    TimeUnit.SECONDS
            );
            if (!mesDto.getStatus().equals(MesStatusEnum.NORMAL)) {
                MesDtoGenerator.scheduleFixParameters(mesDto, this::sendMesDto);
            }
            Thread.sleep(randomBetween(30000, 90000));
            processing.compareAndSet(true, false);
            scadaExecutor.shutdownNow();

            // 3. Анализ
            mesDto.setProcessStatus(MesProcessStatusEnum.ANALYSIS);
            mesDto.setAnalysesTime(LocalDateTime.now());
            sendMesDto(mesDto);
            Thread.sleep(randomBetween(60000, 90000));
            List<LimsDto> limsDtoList = LimsDtoGenerator.generate(batchId, metalType, randomBetween(1, 3));
            sendLimsDto(limsDtoList);

            // 4. Финальный статус
            boolean defect = limsDtoList.stream().anyMatch(l -> l.getStatus().equals(LimsStatusEnum.REJECTED));
            mesDto.setProcessStatus(defect ? MesProcessStatusEnum.DEFECTIVE : MesProcessStatusEnum.ACCEPTED);
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            Double outputYield = defect ? rnd.nextDouble(90, 98) : rnd.nextDouble(98, 100);
            mesDto.setOutputYield(outputYield);
            mesDto.setEndTime(LocalDateTime.now());
            sendMesDto(mesDto);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendMesDto(MesDto dto) {
        if (!producerMes.sendMessage(dto)) {
            throw new RuntimeException("Не удалось отправить MesDto");
        }
    }

    private void sendScadaDto() {
        if (!processing.get()) {
            return;
        }
        ScadaDto dto = ScadaDtoGenerator.generate(equipmentId);
        if (!producerScada.sendMessage(dto)) {
            throw new RuntimeException("Не удалось отправить ScadaDto");
        }
    }

    private void sendLimsDto(List<LimsDto> limsDtoList) {
        for (LimsDto dto : limsDtoList) {
            if (!producerLims.sendMessage(dto)) {
                throw new RuntimeException("Не удалось отправить LimsDto");
            }
        }
    }

    private int randomBetween(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }
}