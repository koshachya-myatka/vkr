package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.datamart.project.models.NotificationEntity;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query(value = "SELECT * FROM fact_notifications as f WHERE f.viewed=false ORDER BY f.created_at DESC;",
            nativeQuery = true)
    List<NotificationEntity> getLastNotifications();
}