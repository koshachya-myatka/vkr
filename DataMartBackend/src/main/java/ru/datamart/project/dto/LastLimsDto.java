package ru.datamart.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LastLimsDto {
    @JsonProperty("sample_id")
    private String sampleId;
    @JsonProperty("metal_type")
    private String metalType;
    @JsonProperty("analysis_method")
    String analysisMethod;
    @JsonProperty("test_date")
    private LocalDateTime testDate;
    @JsonProperty("status_name")
    private String statusName;
}