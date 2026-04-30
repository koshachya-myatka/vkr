package ru.datamart.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.datamart.project.models.MesProcessStatusEnum;
import ru.datamart.project.models.MesStatusEnum;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MesDto {
    @JsonProperty("record_id")
    private String recordId;
    @JsonProperty("batch_id")
    private String batchId;
    @JsonProperty("equipment_id")
    private String equipmentId;
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("end_time")
    private LocalDateTime endTime;
    @JsonProperty("metal_type")
    private String metalType;
    @JsonProperty("process_status")
    private MesProcessStatusEnum processStatus;
    @JsonProperty("operator_id")
    private String operatorId;
    @JsonProperty("output_yield")
    private Double outputYield;
    private Double temperature;
    private Double pressure;
    @JsonProperty("duration_sec")
    private Integer durationSec;
    @JsonProperty("energy_consumption")
    private Double energyConsumption;
    private String additives;
    private MesStatusEnum status;
}