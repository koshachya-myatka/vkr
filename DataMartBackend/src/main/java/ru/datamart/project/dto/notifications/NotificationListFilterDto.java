package ru.datamart.project.dto.notifications;

import lombok.*;
import ru.datamart.project.models.NotificationStatusEnum;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NotificationListFilterDto {
    private Integer offset;
    private Integer limit;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private String equipmentId;
    private String signalSource;
    private String status;
}