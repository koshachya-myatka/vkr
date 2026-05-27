package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.CustomInvalidRequestException;
import ru.datamart.project.dto.batchData.BatchDto;
import ru.datamart.project.dto.batchData.BatchScadaAvgDto;
import ru.datamart.project.dto.batchData.BatchScadaDto;
import ru.datamart.project.dto.batchData.BatchScadaParameterDto;
import ru.datamart.project.dto.kafkaData.ScadaDto;
import ru.datamart.project.models.ScadaEntity;
import ru.datamart.project.repositories.ScadaRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScadaService {
    private final ScadaRepository scadaRepository;
    private final BatchService batchService;

    public List<BatchScadaParameterDto> getScadaByBatchId(String batchId) {
        if (batchId == null) {
            throw new CustomInvalidRequestException("Укажите ID партии.");
        }
        BatchDto batch = batchService.getBatchById(batchId);

        List<BatchScadaDto> raw = (batch.getAnalysesTime() != null)
                ? scadaRepository.findCompressedScadaByBatchId(batchId)
                : scadaRepository.findRealtimeScadaByBatchId(batchId);

        return raw.stream()
                .collect(Collectors.groupingBy(BatchScadaDto::getEquipmentId,
                        Collectors.groupingBy(BatchScadaDto::getParameter)))
                .entrySet()
                .stream()
                .map(entryEquipmentId -> {
                    String equipmentId = entryEquipmentId.getKey();
                    return entryEquipmentId.getValue().entrySet().stream()
                            .map(entryParameter -> {
                                String parameter = entryParameter.getKey();
                                List<BatchScadaDto> values = entryParameter.getValue();
                                values.sort(Comparator.comparing(BatchScadaDto::getTime));
                                return new BatchScadaParameterDto(
                                        equipmentId,
                                        parameter,
                                        values.getFirst().getUnit(),
                                        values
                                );
                            })
                            .toList();
                })
                .flatMap(Collection::stream)
                .toList();
    }

    public List<BatchScadaAvgDto> getScadaAvgByBatchId(String batchId) {
        if (batchId == null) {
            throw new CustomInvalidRequestException("Укажите ID партии.");
        }
        return scadaRepository.findScadaAvgByBatchId(batchId);
    }

    public Optional<ScadaEntity> save(ScadaDto dto) {
        if (dto.getRecordId() == null) {
            throw new CustomInvalidRequestException("Укажите ID записи.");
        }
        ScadaEntity e = new ScadaEntity();
        e.setRecordId(dto.getRecordId());
        e.setSensorId(dto.getSensorId());
        e.setEquipmentId(dto.getEquipmentId());
        e.setTime(dto.getTime());
        e.setParameter(dto.getParameter());
        e.setValue(dto.getValue());
        e.setUnit(dto.getUnit());
        e.setStatus(dto.getStatus());
        ScadaEntity newE = scadaRepository.save(e);
        log.info("СОЗДАНА ИЛИ ОБНОВЛЕНА ЗАПИСЬ SCADA");
        return Optional.of(newE);
    }

    public Optional<ScadaEntity> get(String id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID записи.");
        }
        return scadaRepository.findById(id);
    }

    public void delete(String id) {
        if (id == null) {
            throw new CustomInvalidRequestException("Укажите ID уведомления.");
        }
        scadaRepository.deleteById(id);
        log.info("УДАЛЕНА ЗАПИСЬ SCADA");
    }
}