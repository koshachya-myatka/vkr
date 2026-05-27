package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.datamart.project.dto.batchData.BatchScadaAvgDto;
import ru.datamart.project.dto.batchData.BatchScadaDto;
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
            scada_for_batch AS (
                SELECT s.*
                FROM fact_scada s
                WHERE s.equipment_id IN (
                    SELECT DISTINCT m.equipment_id
                    FROM fact_mes m
                    WHERE m.batch_id = :batchId
                )
                AND s.time BETWEEN (SELECT start_time FROM batch_bounds)
                               AND (SELECT end_time   FROM batch_bounds)
            ),
            normal_bucketed AS (
                SELECT
                    bucketed.equipment_id,
                    bucketed.parameter,
                    bucketed.unit,
                    'NORMAL'::varchar          AS status,
                    MIN(bucketed.time)         AS time,
                    AVG(bucketed.value)::float8 AS value
                FROM (
                    SELECT
                        s.equipment_id,
                        s.parameter,
                        s.unit,
                        s.time,
                        s.value,
                        NTILE(60) OVER (
                            PARTITION BY s.equipment_id, s.parameter
                            ORDER BY s.time
                        ) AS ntile_bucket
                    FROM scada_for_batch s
                    WHERE s.status = 'NORMAL'
                ) bucketed
                GROUP BY
                    bucketed.equipment_id,
                    bucketed.parameter,
                    bucketed.unit,
                    bucketed.ntile_bucket
            ),
            fault_grouped AS (
                SELECT
                    s.equipment_id,
                    s.parameter,
                    s.unit,
                    s.status::varchar,
                    s.time,
                    s.value::float8,
                    ROW_NUMBER() OVER (
                        PARTITION BY s.equipment_id, s.parameter
                        ORDER BY s.time
                    ) -
                    ROW_NUMBER() OVER (
                        PARTITION BY s.equipment_id, s.parameter, s.status
                        ORDER BY s.time
                    ) AS grp
                FROM scada_for_batch s
                WHERE s.status IN ('WARNING', 'ALARM')
            ),
            fault_peaks AS (
                SELECT DISTINCT ON (equipment_id, parameter, status, grp)
                    equipment_id,
                    parameter,
                    unit,
                    status,
                    time,
                    value
                FROM fault_grouped
                ORDER BY equipment_id, parameter, status, grp, value DESC
            ),
            combined AS (
                SELECT
                    equipment_id,
                    parameter,
                    unit,
                    status,
                    time,
                    ROUND(value::numeric, 4) AS value
                FROM normal_bucketed
                UNION ALL
                SELECT
                    equipment_id,
                    parameter,
                    unit,
                    status,
                    time,
                    ROUND(value::numeric, 4) AS value
                FROM fault_peaks
            )
            SELECT
                equipment_id AS equipmentId,
                parameter AS parameter,
                unit AS unit,
                status AS status,
                time AS time,
                CAST (value as DOUBLE PRECISION) AS value
            FROM combined
            ORDER BY parameter, time
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