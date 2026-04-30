package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.datamart.project.models.LimsResultEntity;

public interface LimsResultRepository extends JpaRepository<LimsResultEntity, Long> {
    @Query(value = "DELETE from fact_lims_results res WHERE res.record_id=?1", nativeQuery = true)
    void deleteAllByRecordId(String id);
}