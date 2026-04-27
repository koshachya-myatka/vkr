package ru.datamart.project.generators;

import ru.datamart.project.generators.utils.*;

import java.util.concurrent.*;

public class DataGenerator {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final ExecutorService batchExecutor = Executors.newFixedThreadPool(5);


    public void generate() {
        //todo поправить время
        scheduler.scheduleWithFixedDelay(this::startNewBatch, 0, randomBetween(1, 5), TimeUnit.MINUTES);
    }

    private void startNewBatch() {
        batchExecutor.submit(new BatchProcess());
    }

    private int randomBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}