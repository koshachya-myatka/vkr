package ru.datamart.project.generators.utils;

import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.models.MesProcessStatusEnum;
import ru.datamart.project.models.MesStatusEnum;
import ru.datamart.project.publishers.KafkaProducerLims;
import ru.datamart.project.publishers.KafkaProducerMes;
import ru.datamart.project.publishers.KafkaProducerScada;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchProcess implements Runnable {
    private final ObjectMapper objectMapper;
    private final KafkaProducerMes producerMes;
    private final KafkaProducerScada producerScada;
    private final KafkaProducerLims producerLims;
    private final Runnable onCompleted;

    private final boolean isDefective = ThreadLocalRandom.current().nextDouble() < 0.15;
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
            objectMapper.updateValue(mesDto, MesDtoGenerator.startProcessing(mesDto, isDefective));
            sendMesDto(mesDto);
            List<ScheduledExecutorService> scadaExecutors = startScadaSenders();
            if (!mesDto.getStatus().equals(MesStatusEnum.NORMAL)) {
                MesDtoGenerator.scheduleFixParameters(mesDto, this::sendMesDto);
            }
            Thread.sleep(randomBetween(30000, 120000));
            processing.compareAndSet(true, false);
            scadaExecutors.forEach(ScheduledExecutorService::shutdownNow);

            // 3. Анализ
            mesDto.setProcessStatus(MesProcessStatusEnum.ANALYSIS);
            mesDto.setAnalysesTime(LocalDateTime.now());
            sendMesDto(mesDto);
            Thread.sleep(randomBetween(60000, 90000));
            List<LimsDto> limsDtoList = LimsDtoGenerator.generate(
                    batchId, metalType, randomBetween(1, 3), isDefective
            );
            sendLimsDto(limsDtoList);

            // 4. Финальный статус
            mesDto.setProcessStatus(isDefective ? MesProcessStatusEnum.DEFECTIVE : MesProcessStatusEnum.ACCEPTED);
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            Double outputYield = isDefective ? rnd.nextDouble(90, 98) : rnd.nextDouble(98, 100);
            mesDto.setOutputYield(outputYield);
            mesDto.setEndTime(LocalDateTime.now());
            sendMesDto(mesDto);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            onCompleted.run();
        }
    }

    private List<ScheduledExecutorService> startScadaSenders() {
        List<String> allParams = new ArrayList<>(ScadaDtoGenerator.RANGES.keySet());
        Collections.shuffle(allParams, new Random());
        int count = ThreadLocalRandom.current().nextInt(3, 4);
        List<String> selectedParams = allParams.subList(0, count);

        Map<String, AtomicInteger> sentCounters = new HashMap<>();
        selectedParams.forEach(p -> sentCounters.put(p, new AtomicInteger(0)));

        int faultStart = ThreadLocalRandom.current().nextInt(2, 14);

        List<ScheduledExecutorService> executors = new ArrayList<>();
        for (String param : selectedParams) {
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
                if (!processing.get()) return;
                int idx = sentCounters.get(param).getAndIncrement();
                boolean isFaultMeasurement = isDefective
                        && idx >= faultStart
                        && idx < faultStart + 5;
                ScadaDto dto = ScadaDtoGenerator.generate(equipmentId, param, isFaultMeasurement);
                if (!producerScada.sendMessage(dto)) {
                    throw new RuntimeException("Не удалось отправить ScadaDto");
                }
            }, 0, randomBetween(1, 5), TimeUnit.SECONDS);
            executors.add(exec);
        }
        return executors;
    }

    private void sendMesDto(MesDto dto) {
        if (!producerMes.sendMessage(dto)) {
            throw new RuntimeException("Не удалось отправить MesDto");
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