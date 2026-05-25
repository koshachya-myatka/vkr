package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.CustomEntityNotFoundException;
import ru.datamart.project.customExceptions.CustomInvalidRequestException;
import ru.datamart.project.dto.batchData.BatchDto;
import ru.datamart.project.dto.dashboards.LastBatchDto;
import ru.datamart.project.dto.dashboards.MetalCardDto;
import ru.datamart.project.dto.dashboards.MetalStatisticsCardDto;
import ru.datamart.project.dto.kafkaData.MesDto;
import ru.datamart.project.dto.metalBatches.MetalBatchDto;
import ru.datamart.project.dto.metalBatches.MetalBatchFilterDto;
import ru.datamart.project.dto.other.PageResponseDto;
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

    public PageResponseDto<MetalBatchDto> getMetalBatches(MetalBatchFilterDto dto) {
        if (dto.getOffset() == null || dto.getLimit() == null || dto.getLimit().equals(0)) {
            throw new CustomInvalidRequestException("Кол-во искомых данных не определено.");
        }
        int limit = dto.getLimit();
        List<MetalBatchDto> items = batchRepository.getMetalBatches(
                        dto.getOffset(), limit, dto.getMetalType(),
                        dto.getBatchId(), dto.getStartTime(), dto.getEndTime(),
                        dto.getProcessStatus(), dto.getEquipmentId())
                .stream()
                .peek(d -> {
                    MesProcessStatusEnum status = MesProcessStatusEnum.valueOf(d.getProcessStatus());
                    d.setStatusName(status.toString());
                })
                .toList();
        long totalItems = batchRepository.countMetalBatches(
                dto.getMetalType(), dto.getBatchId(), dto.getStartTime(),
                dto.getEndTime(), dto.getProcessStatus(), dto.getEquipmentId());
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int currentPage = dto.getOffset() / limit + 1;
        return new PageResponseDto<>(items, totalItems, totalPages, currentPage);
    }

    public BatchDto getBatchById(String batchId) {
        if (batchId == null) {
            throw new CustomInvalidRequestException("Укажите ID партии.");
        }
        Optional<BatchDto> optional = batchRepository.getBatchById(batchId);
        if (optional.isEmpty()) {
            throw new CustomEntityNotFoundException("Партия с таким ID не была найдена.");
        }
        BatchDto dto = optional.get();
        MesProcessStatusEnum status = MesProcessStatusEnum.valueOf(dto.getProcessStatus());
        MetalTypeEnum metalType = MetalTypeEnum.valueOf(dto.getMetalType());
        dto.setStatusName(status.toString());
        dto.setMetalTypeName(metalType.toString());
        return dto;
    }

    public DimBatchEntity getOrCreate(MesDto dto) {
        if (dto.getRecordId() == null || dto.getBatchId() == null) {
            throw new CustomInvalidRequestException("Укажите ID записи и партии.");
        }
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