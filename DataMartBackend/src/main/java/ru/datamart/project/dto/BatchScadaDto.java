package ru.datamart.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchScadaDto {
    private String sensorId;
    private String equipmentId;
    private LocalDateTime time;
    private String parameter;
    private Double value;
    private String unit;
    private String status;
    private String statusName;
}