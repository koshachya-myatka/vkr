package ru.datamart.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "fact_notifications")
@Getter
@Setter
@ToString
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @Column(name = "equipment_id")
    private String equipmentId;
    @Column(name = "signal_source")
    private String signalSource;
    @Enumerated(EnumType.STRING)
    private NotificationSeverityEnum severity;
    private Boolean viewed;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}