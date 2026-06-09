package ru.datamart.project.dto.batchData;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchMesDto {
    private String orderId;
    private String equipmentId;
    private String operatorId;
    private Double chargeMass;
    private Double outputMass;
    private Integer durationMin;
}