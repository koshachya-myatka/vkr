package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.datamart.project.dto.BatchMesDto;
import ru.datamart.project.models.MesEntity;

import java.util.Optional;

public interface MesRepository extends JpaRepository<MesEntity, String> {
    @Query(value = """
                SELECT 
                m.equipment_id as equipmentId,
                m.operator_id as operatorId,  
                m.temperature,
                m.pressure,
                m.duration_sec as durationSec,
                m.energy_consumption as energyConsumption,
                m.status,
                '' as statusName
                FROM fact_mes as m
                WHERE m.batch_id = ?1;
            """, nativeQuery = true)
    Optional<BatchMesDto> getMesByBatchId(String batchId);
}