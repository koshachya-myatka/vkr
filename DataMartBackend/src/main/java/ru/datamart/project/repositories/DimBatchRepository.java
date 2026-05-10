package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.datamart.project.dto.*;
import ru.datamart.project.models.DimBatchEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DimBatchRepository extends JpaRepository<DimBatchEntity, String> {
    @Query(value = """
            SELECT 
                b.batch_id as batchId,
                b.metal_type as metalType, 
                '' as metalTypeName,
                b.start_time as startTime, 
                b.end_time as endTime, 
                b.process_status as processStatus,
                '' as statusName
            FROM dim_batch as b
            WHERE b.batch_id = ?1;
            """, nativeQuery = true)
    Optional<BatchDto> getBatchById(String batchId);

    @Query(value = """
            SELECT 
                b.batch_id as batchId                
            FROM dim_batch as b
            JOIN fact_mes as m ON b.batch_id = m.batch_id
            WHERE :time BETWEEN b.start_time AND COALESCE(b.end_time, NOW())
                AND :equipmentId = m.equipment_id;
            """, nativeQuery = true)
    Optional<String> getBatchIdByScada(@Param("time") LocalDateTime time, @Param("equipmentId") String equipmentId);

    @Query(value = """
            SELECT 
                b.metal_type as metalType,
                '' as metalTypeName,
                COUNT(b) as total,
                SUM(CASE WHEN b.process_status = 'ARRIVAL' THEN 1 ELSE 0 END) as arrival,
                SUM(CASE WHEN b.process_status = 'PROCESSING' THEN 1 ELSE 0 END) as processing,
                SUM(CASE WHEN b.process_status = 'ANALYSIS' THEN 1 ELSE 0 END) as analysis,
                SUM(CASE WHEN b.process_status = 'ACCEPTED' THEN 1 ELSE 0 END) as accepted,
                SUM(CASE WHEN b.process_status = 'DEFECTIVE' THEN 1 ELSE 0 END) as defective
            FROM dim_batch as b
            WHERE b.start_time >= CURRENT_DATE AND b.start_time < CURRENT_DATE + INTERVAL '1 day'
            GROUP BY b.metal_type;
            """, nativeQuery = true)
    List<MetalCardDto> getMetalCards();

    @Query(value = """
            SELECT 
                b.batch_id as batchId, 
                b.metal_type as metalType, 
                b.start_time as startTime, 
                b.end_time as endTime, 
                b.process_status as processStatus,
                '' as statusName
            FROM dim_batch as b 
            ORDER BY b.start_time DESC 
            LIMIT 10;
            """, nativeQuery = true)
    List<LastBatchDto> getLastBatches();

    @Query(value = """
            SELECT 
                b.batch_id as batchId,
                m.equipment_id as equipmentId,
                b.start_time as startTime,
                b.end_time as endTime,
                b.process_status as processStatus,
                '' as statusName
            FROM dim_batch as b 
            LEFT JOIN fact_mes as m ON m.batch_id = b.batch_id
            WHERE 
                b.metal_type ILIKE :metalType || '%' AND
                (:batchId IS NULL OR b.batch_id ILIKE :batchId || '%') AND
                (CAST(:startTime AS timestamp) IS NULL OR b.start_time >= CAST(:startTime AS timestamp)) AND
                ((b.end_time IS NULL) OR (CAST(:endTime AS timestamp) IS NULL) OR (b.end_time <= CAST(:endTime AS timestamp))) AND
                (:processStatus IS NULL OR b.process_status ILIKE :processStatus) AND
                (:equipmentId is NULL OR m.equipment_id = :equipmentId)
            ORDER BY b.start_time DESC
            LIMIT 20 OFFSET :offset;
            """, nativeQuery = true)
    List<MetalBatchDto> getMetalBatches(@Param("offset") int offset,
                                        @Param("metalType") String metalType,
                                        @Param("batchId") String batchId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime,
                                        @Param("processStatus") String processStatus,
                                        @Param("equipmentId") String equipmentId);

    @Query(value = """
            SELECT
                metal_type as metalType,
                '' as metalTypeName,
                COUNT(*) as batchesCount,
                CAST(AVG(output_yield) as DOUBLE PRECISION) as averageOutputYield,
                CAST(
                    (
                      (COUNT(*) FILTER (WHERE process_status = 'DEFECTIVE') * 100.0) / NULLIF(COUNT(*), 0)
                    )  as DOUBLE PRECISION
                ) as defectivePercent
            FROM dim_batch
            WHERE process_status IN ('ACCEPTED', 'DEFECTIVE')
              AND end_time >= NOW() - INTERVAL '7 days'
            GROUP BY metal_type
            ORDER BY metal_type;
            """, nativeQuery = true)
    List<MetalStatisticsCardDto> getMetalStatistics();
}