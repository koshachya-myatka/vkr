package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.models.DimBatchEntity;
import ru.datamart.project.repositories.DimBatchRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {
    private final DimBatchRepository repository;

    public DimBatchEntity getOrCreate(MesDto dto) {
        DimBatchEntity batch = repository.findById(dto.getBatchId())
                .orElseGet(() -> {
                    DimBatchEntity b = new DimBatchEntity();
                    b.setBatchId(dto.getBatchId());
                    b.setMetalType(dto.getMetalType());
                    b.setStartTime(dto.getStartTime());
                    b.setProcessStatus(dto.getProcessStatus());
                    return repository.save(b);
                });
        log.info("НАЙДЕН ИЛИ СОЗДАН DIM_BATCH");
        return batch;
    }
}