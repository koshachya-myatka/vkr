package ru.datamart.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MetalBatchDto {
    private String batchId;
    private String equipmentId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String processStatus;
    private String statusName;
}