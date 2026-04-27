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
    String recordId;
    @JsonProperty("parameter_name")
    private String parameterName;
    Float value;
    String unit;
    Boolean normal;
}