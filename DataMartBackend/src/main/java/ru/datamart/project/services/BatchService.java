package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.LastBatchDto;
import ru.datamart.project.dto.MesDto;
import ru.datamart.project.dto.MetalCardDto;
import ru.datamart.project.models.DimBatchEntity;
import ru.datamart.project.models.MesProcessStatusEnum;
import ru.datamart.project.models.MetalTypeEnum;
import ru.datamart.project.repositories.DimBatchRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {
    private final DimBatchRepository batchRepository;

    public DimBatchEntity getOrCreate(MesDto dto) {
        DimBatchEntity batch = batchRepository.findById(dto.getBatchId())
                .orElseGet(() -> {
                    DimBatchEntity b = new DimBatchEntity();
                    b.setBatchId(dto.getBatchId());
                    b.setMetalType(MetalTypeEnum.fromName(dto.getMetalType()));
                    b.setStartTime(dto.getStartTime());
                    b.setProcessStatus(dto.getProcessStatus());
                    return batchRepository.save(b);
                });
        log.info("НАЙДЕН ИЛИ СОЗДАН DIM_BATCH");
        return batch;
    }

    public List<MetalCardDto> getMetalCards() {
        return batchRepository.getMetalCards()
                .stream()
                .map(p -> {
                    MetalTypeEnum metalType = MetalTypeEnum.valueOf(p.getMetalType());
                    return new MetalCardDto(
                            metalType.toString(),
                            p.getTotal(),
                            p.getArrival(),
                            p.getProcessing(),
                            p.getAnalysis(),
                            p.getAccepted(),
                            p.getDefective()
                    );
                })
                .toList();
    }

    public List<LastBatchDto> getLastBatches() {
        return batchRepository.getLastBatches()
                .stream()
                .map(p -> {
                    MesProcessStatusEnum status = MesProcessStatusEnum.valueOf(p.getProcessStatus());
                    return new LastBatchDto(
                            p.getBatchId(),
                            p.getMetalType(),
                            p.getStartTime(),
                            p.getEndTime(),
                            status.toString()
                    );
                })
                .toList();
    }
}