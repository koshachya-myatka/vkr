package ru.datamart.project.dto.notifications;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationListItemDto {
    private Long id;
    private String message;
    private String equipmentId;
    private String sensorId;
    private String signalSource;
    private String severity;
    private String status;
    private Boolean viewed;
    private String comment;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}