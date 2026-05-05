package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.datamart.project.dto.BatchLimsResultDto;
import ru.datamart.project.models.LimsResultEntity;

import java.util.List;

public interface LimsResultRepository extends JpaRepository<LimsResultEntity, Long> {
    @Query(value = "DELETE * FROM fact_lims_results as res WHERE res.record_id=?1;", nativeQuery = true)
    void deleteAllByRecordId(String id);

    @Query(value = """
            SELECT 
                res.parameter_name as parameterName,
                res.value,
                res.unit,
                res.normal
            FROM fact_lims_results as res 
            WHERE res.record_id=?1;
            """, nativeQuery = true)
    List<BatchLimsResultDto> getLimsResultsByRecordId(String recordId);
}