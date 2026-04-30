package ru.datamart.project.generators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.datamart.project.generators.utils.*;
import ru.datamart.project.publishers.KafkaProducerLims;
import ru.datamart.project.publishers.KafkaProducerMes;
import ru.datamart.project.publishers.KafkaProducerScada;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
public class DataGenerator {
    private final ObjectMapper objectMapper;
    private final KafkaProducerMes producerMes;
    private final KafkaProducerScada producerScada;
    private final KafkaProducerLims producerLims;
    //TODO ПРОВЕРИТЬ ПАРАЛЛЕЛИТ ЛИ ОН В НЕСКОЛЬКО ПОТОКОВ
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final ExecutorService batchExecutor = Executors.newFixedThreadPool(5);

    public void generate() {
        scheduler.scheduleWithFixedDelay(this::startNewBatch, 0,
                ThreadLocalRandom.current().nextInt(30, 180), TimeUnit.SECONDS);
    }

    private void startNewBatch() {
        batchExecutor.submit(new BatchProcess(objectMapper, producerMes, producerScada, producerLims));
    }
}