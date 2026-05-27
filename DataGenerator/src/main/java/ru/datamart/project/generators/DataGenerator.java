package ru.datamart.project.generators;

import jakarta.annotation.PostConstruct;
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

    private final ExecutorService batchExecutor = Executors.newFixedThreadPool(5);
    private final ScheduledExecutorService restartScheduler = Executors.newScheduledThreadPool(5);

    public void generate() {
        for (int i = 0; i < 5; i++) {
            submitBatch();
        }
    }

    private void submitBatch() {
        batchExecutor.submit(() -> {
            try {
                BatchProcess batch = new BatchProcess(
                        objectMapper, producerMes, producerScada, producerLims,
                        this::onBatchCompleted
                );
                batch.run();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void onBatchCompleted() {
        long delaySeconds = ThreadLocalRandom.current().nextLong(60, 301);
        restartScheduler.schedule(this::submitBatch, delaySeconds, TimeUnit.SECONDS);
    }
}