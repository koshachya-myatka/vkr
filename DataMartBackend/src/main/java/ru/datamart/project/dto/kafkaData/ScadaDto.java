package ru.datamart.project.dto.kafkaData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.datamart.project.models.ScadaStatusEnum;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScadaDto {
    @JsonProperty("record_id")
    private String recordId;
    @JsonProperty("sensor_id")
    private String sensorId;
    @JsonProperty("equipment_id")
    private String equipmentId;
    private LocalDateTime time;
    private String parameter;
    private Double value;
    private String unit;
    private ScadaStatusEnum status;
}