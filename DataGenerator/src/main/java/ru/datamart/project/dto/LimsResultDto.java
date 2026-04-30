package ru.datamart.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LimsResultDto {
    @JsonProperty("record_id")
    private String recordId;
    @JsonProperty("parameter_name")
    private String parameterName;
    private Float value;
    private String unit;
    private Boolean normal;
}