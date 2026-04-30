package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.MesDto;
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

    public Optional<MesEntity> save(MesDto dto) {
        DimBatchEntity batch = batchService.getOrCreate(dto);
        batch.setProcessStatus(dto.getProcessStatus());
        batch.setEndTime(dto.getEndTime());
        batch.setOutputYield(dto.getOutputYield());
        batchRepo.save(batch);
        log.info("ОБНОВЛЕН DIM_BATCH");

        MesEntity e = new MesEntity();
        e.setRecordId(dto.getRecordId());
        e.setBatch(batch);
        e.setEquipmentId(dto.getEquipmentId());
        e.setOperatorId(dto.getOperatorId());
        e.setTemperature(dto.getTemperature());
        e.setPressure(dto.getPressure());
        e.setDurationSec(dto.getDurationSec());
        e.setEnergyConsumption(dto.getEnergyConsumption());
        e.setAdditives(dto.getAdditives());
        e.setStatus(dto.getStatus());
        MesEntity newE = mesRepo.save(e);
        log.info("СОЗДАНА ИЛИ ОБНОВЛЕНА ЗАПИСЬ MES");
        return Optional.of(newE);
    }

    public Optional<MesEntity> get(String id) {
        return mesRepo.findById(id);
    }

    public void delete(String id) {
        mesRepo.deleteById(id);
        log.info("УДАЛЕНА ЗАПИСЬ MES");
    }
}