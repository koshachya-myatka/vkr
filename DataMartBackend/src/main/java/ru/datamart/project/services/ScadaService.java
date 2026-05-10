package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.BatchScadaDto;
import ru.datamart.project.dto.BatchScadaParameterDto;
import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.models.ScadaEntity;
import ru.datamart.project.models.ScadaStatusEnum;
import ru.datamart.project.repositories.ScadaRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScadaService {
    private final ScadaRepository scadaRepository;

    public List<BatchScadaParameterDto> getScadaByBatchId(String batchId) {
        List<BatchScadaDto> raw = scadaRepository.findScadaByBatchId(batchId).stream()
                .peek(dto -> {
                    ScadaStatusEnum status = ScadaStatusEnum.valueOf(dto.getStatus());
                    dto.setStatusName(status.toString());
                })
                .toList();
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

    public Optional<ScadaEntity> save(ScadaDto dto) {
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
        return scadaRepository.findById(id);
    }

    public void delete(String id) {
        scadaRepository.deleteById(id);
        log.info("УДАЛЕНА ЗАПИСЬ SCADA");
    }
}