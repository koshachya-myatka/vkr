package ru.datamart.project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.BatchLimsDto;
import ru.datamart.project.dto.LastLimsDto;
import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.LimsResultDto;
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
        return limsRepo.getLimsByBatchId(batchId)
                .stream()
                .peek(dto -> {
                    LimsStatusEnum status = LimsStatusEnum.valueOf(dto.getStatus());
                    dto.setStatusName(status.toString());
                    dto.setResults(resultRepo.getLimsResultsByRecordId(dto.getRecordId()));
                })
                .toList();
    }

    public List<BatchLimsDto> getLimsWithoutResultsByBatchId(String batchId) {
        return limsRepo.getLimsByBatchId(batchId)
                .stream()
                .peek(dto -> {
                    LimsStatusEnum status = LimsStatusEnum.valueOf(dto.getStatus());
                    dto.setStatusName(status.toString());
                })
                .toList();
    }

    @Transactional
    public Optional<LimsEntity> save(LimsDto dto) {
        DimBatchEntity batch = batchRepo.findById(dto.getBatchId())
                .orElseThrow();

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
        return limsRepo.findById(id);
    }

    public void delete(String id) {
        limsRepo.deleteById(id);
        resultRepo.deleteAllByRecordId(id);
        log.info("УДАЛЕНА ЗАПИСЬ LIMS");
    }
}