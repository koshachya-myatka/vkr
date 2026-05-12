package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.datamart.project.dto.BatchScadaAvgDto;
import ru.datamart.project.dto.BatchScadaDto;
import ru.datamart.project.models.ScadaEntity;

import java.util.List;

@Repository
public interface ScadaRepository extends JpaRepository<ScadaEntity, String> {
    @Query(value = """
            SELECT
                s.equipment_id as equipmentId,
                s.parameter as parameter,
                s.time as time,
                s.value as value,
                s.unit as unit,
                s.status as status
            FROM fact_scada as s
            WHERE s.equipment_id IN (
                SELECT DISTINCT m.equipment_id
                FROM fact_mes as m
                WHERE m.batch_id = :batchId
            )
            AND s.time BETWEEN
                (SELECT b.start_time FROM dim_batch as b WHERE b.batch_id = :batchId)
            AND
                (SELECT COALESCE(b.end_time, NOW()) FROM dim_batch as b WHERE b.batch_id = :batchId)
            ORDER BY s.parameter, s.time;
            """, nativeQuery = true)
    List<BatchScadaDto> findScadaByBatchId(@Param("batchId") String batchId);

    @Query(value = """
            SELECT
                s.equipment_id as equipmentId,
                s.parameter as parameter,
                s.unit as unit,
                CAST (ROUND(AVG(s.value)::numeric, 2) as DOUBLE PRECISION) as avgValue,
                CAST (ROUND(MIN(s.value)::numeric, 2) as DOUBLE PRECISION) as minValue,
                CAST (ROUND(MAX(s.value)::numeric, 2) as DOUBLE PRECISION) as maxValue,
                COUNT(*) as valuesCount
            FROM fact_scada as s
            WHERE s.equipment_id IN (
                SELECT DISTINCT m.equipment_id
                FROM fact_mes as m
                WHERE m.batch_id = :batchId
            )
            AND s.time BETWEEN
                (SELECT b.start_time FROM dim_batch as b WHERE b.batch_id = :batchId)
            AND
                (SELECT COALESCE(b.end_time, NOW()) FROM dim_batch as b WHERE b.batch_id = :batchId)
            GROUP BY
                s.equipment_id,
                s.parameter,
                s.unit
            ORDER BY
                s.equipment_id,
                s.parameter
            """, nativeQuery = true)
    List<BatchScadaAvgDto> findScadaAvgByBatchId(@Param("batchId") String batchId);
}