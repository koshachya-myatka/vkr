package ru.datamart.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MetalBatchFilterDto {
    private String metalType;
    private String batchId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String processStatus;
    private Integer offset;
    private String equipmentId;
}