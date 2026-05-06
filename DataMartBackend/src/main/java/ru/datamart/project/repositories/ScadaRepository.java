package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.datamart.project.dto.BatchScadaDto;
import ru.datamart.project.models.ScadaEntity;

import java.util.List;

public interface ScadaRepository extends JpaRepository<ScadaEntity, String> {
    @Query(value = """
            SELECT DISTINCT ON (s.parameter)
                s.sensor_id as sensorId,
                s.equipment_id as equipmentId,
                s.time
                s.parameter,
                s.value,
                s.unit,
                s.status,
                '' as statusName
            FROM fact_scada as s
            WHERE s.equipment_id IN (
                SELECT DISTINCT m.equipment_id
                FROM fact_mes as m
                WHERE m.batch_id = :batchId
            )
            AND s.time BETWEEN
                (SELECT b.start_time FROM dim_batch as b WHERE b.batch_id = :batchId
            AND
                (SELECT COALESCE(b.end_time, NOW()) FROM dim_batch as b WHERE b.batch_id = :batchId
            ORDER BY s.parameter, s.time DESC;
            """, nativeQuery = true)
    List<BatchScadaDto> findScadaByBatchId(@Param("batchId") String batchId);
}