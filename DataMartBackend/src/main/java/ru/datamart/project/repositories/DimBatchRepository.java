package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.datamart.project.dto.LastBatchDto;
import ru.datamart.project.dto.MetalCardDto;
import ru.datamart.project.models.DimBatchEntity;

import java.util.List;

public interface DimBatchRepository extends JpaRepository<DimBatchEntity, String> {
    @Query(value =
            """
            SELECT 
                b.metal_type as metalType,
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
}