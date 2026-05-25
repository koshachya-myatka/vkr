package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.datamart.project.dto.notifications.NotificationListItemDto;
import ru.datamart.project.dto.notifications.NotificationStatsDto;
import ru.datamart.project.models.NotificationEntity;

import java.time.LocalDateTime;
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

    @Query(value = """
            SELECT
                n.id as id,
                n.message as message,
                n.equipment_id as equipmentId,
                n.sensor_id as sensorId,
                n.signal_source as signalSource,
                n.severity as severity,
                n.status as status,
                n.viewed as viewed,
                n.comment as comment,
                n.updated_by as updatedBy,
                n.created_at as createdAt,
                n.updated_at as updatedAt
            FROM fact_notifications as n
            WHERE
                ((:equipmentId IS NULL) OR (n.equipment_id ILIKE CONCAT('%', :equipmentId, '%')))
            AND
                ((:signalSource IS NULL) OR (n.signal_source = :signalSource))
            AND
                ((:status IS NULL) OR (n.status = :status))
            AND
                ((CAST(:dateFrom AS timestamp) IS NULL) OR (n.created_at >= CAST(:dateFrom AS timestamp)))
            AND
                ((CAST(:dateTo AS timestamp) IS NULL) OR (n.created_at <= CAST(:dateTo AS timestamp)))
            ORDER BY n.created_at DESC
            LIMIT :limit OFFSET :offset;
            """,
            nativeQuery = true)
    List<NotificationListItemDto> getNotifications(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("equipmentId") String equipmentId,
            @Param("signalSource") String signalSource,
            @Param("status") String status,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo
    );

    @Query(value = """
            SELECT COUNT(n.id)
            FROM fact_notifications as n
            WHERE
                ((:equipmentId IS NULL) OR (n.equipment_id ILIKE CONCAT('%', :equipmentId, '%')))
            AND
                ((:signalSource IS NULL) OR (n.signal_source = :signalSource))
            AND
                ((:status IS NULL) OR (n.status = :status))
            AND
                ((CAST(:dateFrom AS timestamp) IS NULL) OR (n.created_at >= CAST(:dateFrom AS timestamp)))
            AND
                ((CAST(:dateTo AS timestamp) IS NULL) OR (n.created_at <= CAST(:dateTo AS timestamp)));
            """,
            nativeQuery = true)
    long countNotifications(
            @Param("equipmentId") String equipmentId,
            @Param("signalSource") String signalSource,
            @Param("status") String status,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo
    );

    @Query(value = """
            SELECT
                COUNT(*) as totalToday,
                COUNT(*) FILTER (WHERE n.status = 'CREATED') as createdCount,
                COUNT(*) FILTER (WHERE n.status = 'IN_PROGRESS') as inProgressCount,
                COUNT(*) FILTER (WHERE n.status = 'FALSE_POSITIVE') as falsePositiveCount,
                COUNT(*) FILTER (WHERE n.status = 'RESOLVED') as resolvedCount
            FROM fact_notifications as n
            WHERE DATE(n.created_at) = CURRENT_DATE;
            """,
            nativeQuery = true)
    NotificationStatsDto getStats();
}