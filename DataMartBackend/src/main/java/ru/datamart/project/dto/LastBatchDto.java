package ru.datamart.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LastBatchDto {
    @JsonProperty("batch_id")
    private String batchId;
    @JsonProperty("metal_type")
    private String metalType;
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("end_time")
    private LocalDateTime endTime;
    @JsonProperty("status_name")
    private String statusName;
}
