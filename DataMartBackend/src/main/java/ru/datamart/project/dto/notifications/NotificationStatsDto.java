package ru.datamart.project.dto.notifications;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationStatsDto {
    private Long totalToday;
    private Long createdCount;
    private Long inProgressCount;
    private Long falsePositiveCount;
    private Long resolvedCount;
}