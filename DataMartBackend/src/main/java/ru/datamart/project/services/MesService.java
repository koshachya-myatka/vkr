package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.CustomEntityNotFoundException;
import ru.datamart.project.customExceptions.CustomInvalidRequestException;
import ru.datamart.project.dto.batchData.BatchMesDto;
import ru.datamart.project.dto.kafkaData.MesDto;
import ru.datamart.project.models.DimBatchEntity;
import ru.datamart.project.models.MesEntity;
import ru.datamart.project.repositories.DimBatchRepository;
import ru.datamart.project.repositories.MesRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MesService {
    private final BatchService batchService;
    private final MesRepository mesRepo;
    private final DimBatchRepository batchRepo;

    public BatchMesDto getMesByBatchId(String batchId) {
        if (batchId == null) {
            throw new CustomInvalidRequestException("Укажите ID партии.");
        }
        Optional<BatchMesDto> optional = mesRepo.getMesByBatchId(batchId);
        if (optional.isEmpty()) {
            throw new CustomEntityNotFoundException("Данные MES для партии не были найдены.");
        }
        return optional.get();
    }

    public Optional<MesEntity> save(MesDto dto) {
        if (dto.getRecordId() == null || dto.getBatchId() == null) {
            throw new CustomInvalidRequestException("Укажите ID записи и партии.");
        }
        DimBatchEntity batch = batchService.getOrCreate(dto);
        batch.setProcessStatus(dto.getProcessStatus());
        batch.setProcessingTime(dto.getProcessingTime());
        batch.setAnalysesTime(dto.getAnalysesTime());
        batch.setEndTime(dto.getEndTime());
        batch.setOutputYield(dto.getOutputYield());
        batchRepo.save(batch);
        log.info("ОБНОВЛЕН DIM_BATCH");

        MesEntity e = new MesEntity();
        e.setRecordId(dto.getRecordId());
        e.setOrderId(dto.getOrderId());
        e.setBatch(batch);
        e.setEquipmentId(dto.getEquipmentId());
        e.setOperatorId(dto.getOperatorId());
        e.setChargeMass(dto.getChargeMass());
        e.setOutputMass(dto.getOutputMass());
        e.setDurationMin(dto.getDurationMin());
        MesEntity newE = mesRepo.save(e);
        log.info("СОЗДАНА ИЛИ ОБНОВЛЕНА ЗАПИСЬ MES");
        return Optional.of(newE);
    }

    public Optional<MesEntity> get(String id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID записи.");
        }
        return mesRepo.findById(id);
    }

    public void delete(String id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID записи.");
        }
        mesRepo.deleteById(id);
        log.info("УДАЛЕНА ЗАПИСЬ MES");
    }
}