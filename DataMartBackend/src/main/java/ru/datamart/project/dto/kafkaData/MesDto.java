package ru.datamart.project.dto.kafkaData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.datamart.project.models.MesProcessStatusEnum;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MesDto {
    @JsonProperty("record_id")
    private String recordId;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("batch_id")
    private String batchId;
    @JsonProperty("equipment_id")
    private String equipmentId;
    @JsonProperty("operator_id")
    private String operatorId;
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("processing_time")
    private LocalDateTime processingTime;
    @JsonProperty("analyses_time")
    private LocalDateTime analysesTime;
    @JsonProperty("end_time")
    private LocalDateTime endTime;
    @JsonProperty("metal_type")
    private String metalType;
    @JsonProperty("process_status")
    private MesProcessStatusEnum processStatus;
    @JsonProperty("charge_mass")
    private Double chargeMass;
    @JsonProperty("output_mass")
    private Double outputMass;
    @JsonProperty("output_yield")
    private Double outputYield;
    @JsonProperty("duration_min")
    private Integer durationMin;
}