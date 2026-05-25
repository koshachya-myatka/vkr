package ru.datamart.project.dto.metalBatches;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MetalBatchFilterDto {
    private Integer offset;
    private Integer limit;
    private String metalType;
    private String batchId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String processStatus;
    private String equipmentId;
}