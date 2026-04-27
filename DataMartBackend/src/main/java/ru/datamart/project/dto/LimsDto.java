package ru.datamart.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.datamart.project.models.LimsStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LimsDto {
    @JsonProperty("record_id")
    String recordId;
    @JsonProperty("batch_id")
    String batchId;
    @JsonProperty("sample_id")
    String sampleId;
    @JsonProperty("analysis_method")
    String analysisMethod;
    @JsonProperty("test_date")
    LocalDateTime testDate;
    LimsStatusEnum status;
    List<LimsResultDto> results;
}