package ru.datamart.project.dto.dashboards;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LastBatchDto {
    private String batchId;
    private String metalType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String processStatus;
    private String statusName;
}
