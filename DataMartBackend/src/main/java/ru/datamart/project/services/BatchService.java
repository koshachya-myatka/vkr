package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.*;
import ru.datamart.project.models.DimBatchEntity;
import ru.datamart.project.models.MesProcessStatusEnum;
import ru.datamart.project.models.MetalTypeEnum;
import ru.datamart.project.models.ScadaEntity;
import ru.datamart.project.repositories.DimBatchRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {
    private final DimBatchRepository batchRepository;

    public Optional<String> getBatchIdByScada(ScadaEntity scada) {
        return batchRepository.getBatchIdByScada(scada.getTime(), scada.getEquipmentId());
    }

    public List<MetalStatisticsCardDto> getMetalStatisticsCards() {
        return batchRepository.getMetalStatistics()
                .stream()
                .peek(dto -> {
                    MetalTypeEnum metalType = MetalTypeEnum.valueOf(dto.getMetalType());
                    dto.setMetalTypeName(metalType.toString());
                })
                .toList();
    }

    public List<MetalCardDto> getMetalCards() {
        return batchRepository.getMetalCards()
                .stream()
                .peek(dto -> {
                    MetalTypeEnum metalType = MetalTypeEnum.valueOf(dto.getMetalType());
                    dto.setMetalTypeName(metalType.toString());
                })
                .toList();
    }

    public List<LastBatchDto> getLastBatches() {
        return batchRepository.getLastBatches()
                .stream()
                .peek(dto -> {
                    MesProcessStatusEnum status = MesProcessStatusEnum.valueOf(dto.getProcessStatus());
                    MetalTypeEnum metalType = MetalTypeEnum.valueOf(dto.getMetalType());
                    dto.setStatusName(status.toString());
                    dto.setMetalType(metalType.toString());
                })
                .toList();
    }

    public List<MetalBatchDto> getMetalBatches(MetalBatchFilterDto dto) {
        return batchRepository.getMetalBatches(dto.getOffset(), dto.getMetalType(), dto.getBatchId(), dto.getStartTime(),
                        dto.getEndTime(), dto.getProcessStatus(), dto.getEquipmentId())
                .stream()
                .peek(d -> {
                    MesProcessStatusEnum status = MesProcessStatusEnum.valueOf(d.getProcessStatus());
                    d.setStatusName(status.toString());
                })
                .toList();
    }

    public BatchDto getBatchById(String batchId) {
        Optional<BatchDto> optional = batchRepository.getBatchById(batchId);
        if (optional.isEmpty()) {
            return null;
        }
        BatchDto dto = optional.get();
        MesProcessStatusEnum status = MesProcessStatusEnum.valueOf(dto.getProcessStatus());
        MetalTypeEnum metalType = MetalTypeEnum.valueOf(dto.getMetalType());
        dto.setStatusName(status.toString());
        dto.setMetalTypeName(metalType.toString());
        return dto;
    }

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
}