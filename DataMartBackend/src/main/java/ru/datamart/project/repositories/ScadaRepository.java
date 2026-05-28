package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.datamart.project.dto.batchData.BatchScadaAvgDto;
import ru.datamart.project.dto.batchData.BatchScadaDto;
import ru.datamart.project.models.ScadaEntity;
import ru.datamart.project.models.ScadaEntityId;

import java.util.List;

@Repository
public interface ScadaRepository extends JpaRepository<ScadaEntity, ScadaEntityId> {
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
            AND (s.time >= NOW() - INTERVAL '1 minutes' OR s.status IN ('WARNING', 'ALARM'))
            ORDER BY s.parameter, s.time;
            """, nativeQuery = true)
    List<BatchScadaDto> findRealtimeScadaByBatchId(@Param("batchId") String batchId);

    @Query(value = """
            WITH batch_bounds AS (
                SELECT
                    b.start_time,
                    COALESCE(b.end_time, NOW()) AS end_time
                FROM dim_batch b
                WHERE b.batch_id = :batchId
            ),
            batch_equipment AS (
                SELECT DISTINCT m.equipment_id
                FROM fact_mes m
                WHERE m.batch_id = :batchId
            ),
            scada_in_batch AS (
                SELECT
                    s.equipment_id,
                    s.parameter,
                    s.time,
                    s.value,
                    s.unit,
                    s.status,
                    bb.start_time,
                    bb.end_time
                FROM fact_scada s
                JOIN batch_equipment be ON s.equipment_id = be.equipment_id
                CROSS JOIN batch_bounds bb
                WHERE s.time BETWEEN bb.start_time AND bb.end_time
            ),
            normal_data AS (
                SELECT
                    equipment_id,
                    parameter,
                    time,
                    value,
                    unit,
                    status,
                    FLOOR(
                        EXTRACT(EPOCH FROM (time - start_time)) /
                        NULLIF(EXTRACT(EPOCH FROM (end_time - start_time)) / 60.0, 0)
                    ) AS bucket
                FROM scada_in_batch
                WHERE status = 'NORMAL'
            ),
            normal_compressed AS (
                SELECT
                    equipment_id,
                    parameter,
                    unit,
                    status,
                    CAST(ROUND(AVG(value)::numeric, 2) AS DOUBLE PRECISION) AS value,
                    MIN(time) AS time
                FROM normal_data
                GROUP BY equipment_id, parameter, unit, status, bucket
            ),
            anomaly_data AS (
                SELECT
                    equipment_id,
                    parameter,
                    time,
                    value,
                    unit,
                    status,
                    LAG(status) OVER (
                        PARTITION BY equipment_id, parameter
                        ORDER BY time
                    ) AS prev_status,
                    LAG(time) OVER (
                        PARTITION BY equipment_id, parameter
                        ORDER BY time
                    ) AS prev_time
                FROM scada_in_batch
                WHERE status IN ('WARNING', 'ALARM')
            ),
            anomaly_with_groups AS (
                SELECT
                    equipment_id,
                    parameter,
                    time,
                    value,
                    unit,
                    status,
                    SUM(
                        CASE WHEN prev_status IS NULL
                                  OR prev_status != status
                                  OR EXTRACT(EPOCH FROM (time - prev_time)) > 60
                        THEN 1 ELSE 0 END
                    ) OVER (
                        PARTITION BY equipment_id, parameter
                        ORDER BY time
                        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
                    ) AS grp
                FROM anomaly_data
            ),
            anomaly_compressed AS (
                SELECT
                    equipment_id,
                    parameter,
                    unit,
                    status,
                    CAST(ROUND(AVG(value)::numeric, 2) AS DOUBLE PRECISION) AS value,
                    MIN(time) AS time
                FROM anomaly_with_groups
                GROUP BY equipment_id, parameter, unit, status, grp
            )
            SELECT equipment_id AS equipmentId,
                   parameter,
                   time,
                   value,
                   unit,
                   status
            FROM normal_compressed
            
            UNION ALL
            
            SELECT equipment_id AS equipmentId,
                   parameter,
                   time,
                   value,
                   unit,
                   status
            FROM anomaly_compressed
            
            ORDER BY parameter, time;
            """, nativeQuery = true)
    List<BatchScadaDto> findCompressedScadaByBatchId(@Param("batchId") String batchId);

    @Query(value = """
            SELECT
                s.equipment_id as equipmentId,
                s.parameter as parameter,
                s.unit as unit,
                CAST (ROUND(AVG(s.value)::numeric, 2) as DOUBLE PRECISION) as avgValue,
                CAST (ROUND(MIN(s.value)::numeric, 2) as DOUBLE PRECISION) as minValue,
                CAST (ROUND(MAX(s.value)::numeric, 2) as DOUBLE PRECISION) as maxValue,
                COUNT(*) as valuesCount,
                COUNT(*) FILTER (WHERE s.status <> 'NORMAL') as alarmCount
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