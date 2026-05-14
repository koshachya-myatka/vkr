package ru.datamart.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchDto {
    private String batchId;
    private String metalType;
    private String metalTypeName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String processStatus;
    private String statusName;
    private Double outputYield;
}