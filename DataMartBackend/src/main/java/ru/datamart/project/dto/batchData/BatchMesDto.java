package ru.datamart.project.dto.batchData;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchMesDto {
    private String equipmentId;
    private String operatorId;
    private Double temperature;
    private Double pressure;
    private Integer durationSec;
    private Double energyConsumption;
    private String status;
    private String statusName;
}
