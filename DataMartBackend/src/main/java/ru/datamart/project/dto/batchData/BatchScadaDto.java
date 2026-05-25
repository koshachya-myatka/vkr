package ru.datamart.project.dto.batchData;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchScadaDto {
    private String equipmentId;
    private String parameter;
    private LocalDateTime time;
    private Double value;
    private String unit;
    private String status;
}