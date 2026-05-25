package ru.datamart.project.dto.batchData;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchScadaAvgDto {
    private String equipmentId;
    private String parameter;
    private String unit;
    private Double avgValue;
    private Double minValue;
    private Double maxValue;
    private Long valuesCount;
}