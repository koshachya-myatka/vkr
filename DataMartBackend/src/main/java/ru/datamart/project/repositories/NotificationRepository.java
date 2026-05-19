package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.datamart.project.models.NotificationEntity;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query(value = "SELECT * FROM fact_notifications as f WHERE f.viewed=false ORDER BY f.created_at DESC;",
            nativeQuery = true)
    List<NotificationEntity> getActive();

    @Query(value = """
            SELECT COUNT(*)
            FROM fact_notifications as n
            WHERE n.equipment_id IN (
                SELECT DISTINCT m.equipment_id
                FROM fact_mes as m
                WHERE m.batch_id = :batchId
            )
            AND n.created_at BETWEEN
                (SELECT b.start_time FROM dim_batch as b WHERE b.batch_id = :batchId)
            AND
                COALESCE((SELECT b.end_time FROM dim_batch as b WHERE b.batch_id = :batchId), NOW())
            """, nativeQuery = true)
    long countAlarmsByBatchId(@Param("batchId") String batchId);
}