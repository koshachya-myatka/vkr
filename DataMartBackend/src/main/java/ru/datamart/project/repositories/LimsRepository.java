package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.datamart.project.dto.LastLimsDto;
import ru.datamart.project.models.LimsEntity;

import java.util.List;

public interface LimsRepository extends JpaRepository<LimsEntity, String> {
    @Query(value = """
            SELECT 
                l.sample_id as sampleId,
                b.metal_type as metalType,
                l.analysis_method as analysisMethod,
                l.test_date as testDate,
                l.status as status,
                '' as statusName
            FROM fact_lims as l JOIN dim_batch as b ON l.batch_id=b.batch_id
            ORDER BY l.test_date DESC
            LIMIT 10;
            """, nativeQuery = true)
    List<LastLimsDto> getLastLimsRecords();
}