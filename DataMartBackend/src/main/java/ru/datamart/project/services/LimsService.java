package ru.datamart.project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.LastLimsDto;
import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.LimsResultDto;
import ru.datamart.project.models.DimBatchEntity;
import ru.datamart.project.models.LimsEntity;
import ru.datamart.project.models.LimsResultEntity;
import ru.datamart.project.models.LimsStatusEnum;
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
                .map(p -> {
                    LimsStatusEnum status = LimsStatusEnum.valueOf(p.getStatus());
                    return new LastLimsDto(
                            p.getSampleId(),
                            p.getMetalType(),
                            p.getAnalysisMethod(),
                            p.getTestDate(),
                            status.toString()
                    );
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