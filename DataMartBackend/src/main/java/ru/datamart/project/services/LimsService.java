package ru.datamart.project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.CustomEntityNotFoundException;
import ru.datamart.project.customExceptions.CustomInvalidRequestException;
import ru.datamart.project.dto.batchData.BatchLimsDto;
import ru.datamart.project.dto.dashboards.LastLimsDto;
import ru.datamart.project.dto.kafkaData.LimsDto;
import ru.datamart.project.dto.kafkaData.LimsResultDto;
import ru.datamart.project.models.*;
import ru.datamart.project.repositories.DimBatchRepository;
import ru.datamart.project.repositories.LimsRepository;
import ru.datamart.project.repositories.LimsResultRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LimsService {
    private final DimBatchRepository batchRepo;
    private final LimsRepository limsRepo;
    private final LimsResultRepository resultRepo;

    public List<LastLimsDto> getLastLims() {
        return limsRepo.getLastLimsRecords()
                .stream()
                .peek(dto -> {
                    LimsStatusEnum status = LimsStatusEnum.valueOf(dto.getStatus());
                    MetalTypeEnum metalType = MetalTypeEnum.valueOf(dto.getMetalType());
                    dto.setMetalType(metalType.toString());
                    dto.setStatusName(status.toString());
                })
                .toList();
    }

    public List<BatchLimsDto> getLimsByBatchId(String batchId) {
        if (batchId == null) {
            throw new CustomInvalidRequestException("Укажите ID партии.");
        }
        return limsRepo.getLimsByBatchIdWithoutResults(batchId)
                .stream()
                .peek(dto -> {
                    LimsStatusEnum status = LimsStatusEnum.valueOf(dto.getStatus());
                    dto.setStatusName(status.toString());
                    dto.setResults(resultRepo.getLimsResultsByRecordId(dto.getRecordId()));
                })
                .toList();
    }

    public List<BatchLimsDto> getLimsWithoutResultsByBatchId(String batchId) {
        if (batchId == null) {
            throw new CustomInvalidRequestException("Укажите ID партии.");
        }
        return limsRepo.getLimsByBatchIdWithoutResults(batchId)
                .stream()
                .peek(dto -> {
                    LimsStatusEnum status = LimsStatusEnum.valueOf(dto.getStatus());
                    dto.setStatusName(status.toString());
                })
                .toList();
    }

    @Transactional
    public Optional<LimsEntity> save(LimsDto dto) {
        if (dto.getRecordId() == null || dto.getBatchId() == null) {
            throw new CustomInvalidRequestException("Укажите ID записи и партии.");
        }
        DimBatchEntity batch = batchRepo.findById(dto.getBatchId())
                .orElseThrow(() -> new CustomEntityNotFoundException("Партия с таким ID не найдена."));

        LimsEntity e = new LimsEntity();
        e.setRecordId(dto.getRecordId());
        e.setBatch(batch);
        e.setSampleId(dto.getSampleId());
        e.setAnalysisMethod(dto.getAnalysisMethod());
        e.setTestDate(dto.getTestDate());
        e.setStatus(dto.getStatus());
        LimsEntity newE = limsRepo.save(e);

        for (LimsResultDto r : dto.getResults()) {
            LimsResultEntity re = new LimsResultEntity();
            re.setLims(e);
            re.setParameterName(r.getParameterName());
            re.setValue(r.getValue());
            re.setUnit(r.getUnit());
            re.setNormal(r.getNormal());
            resultRepo.save(re);
        }
        log.info("СОЗДАНА ИЛИ ОБНОВЛЕНА ЗАПИСЬ LIMS");
        return Optional.of(newE);
    }

    public Optional<LimsEntity> get(String id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID записи.");
        }
        return limsRepo.findById(id);
    }

    public void delete(String id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID записи.");
        }
        limsRepo.deleteById(id);
        resultRepo.deleteAllByRecordId(id);
        log.info("УДАЛЕНА ЗАПИСЬ LIMS");
    }
}