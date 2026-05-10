package ru.datamart.project.services;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ProcessingBatchRegistry {
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<String>> processingBatches =
            new ConcurrentHashMap<>();

    public void addBatch(String equipmentId, String batchId) {
        processingBatches.computeIfAbsent(equipmentId, key -> new CopyOnWriteArraySet<>())
                .add(batchId);
    }

    public void removeBatch(String equipmentId, String batchId) {
        CopyOnWriteArraySet<String> batches = processingBatches.get(equipmentId);
        if (batches == null) {
            return;
        }
        batches.remove(batchId);
        if (batches.isEmpty()) {
            processingBatches.remove(equipmentId);
        }
    }

    public Set<String> getBatchIds(String equipmentId) {
        return processingBatches.getOrDefault(equipmentId, new CopyOnWriteArraySet<>());
    }
}