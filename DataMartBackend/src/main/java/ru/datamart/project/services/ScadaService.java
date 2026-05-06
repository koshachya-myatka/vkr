package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.BatchScadaDto;
import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.models.ScadaEntity;
import ru.datamart.project.repositories.ScadaRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScadaService {
    private final ScadaRepository scadaRepository;

    public List<BatchScadaDto> getScadaByBatchId(String batchId) {
        return scadaRepository.findScadaByBatchId(batchId);
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