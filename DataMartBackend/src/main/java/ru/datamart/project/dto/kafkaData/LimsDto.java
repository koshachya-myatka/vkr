package ru.datamart.project.dto.kafkaData;

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
    private String recordId;
    @JsonProperty("batch_id")
    private String batchId;
    @JsonProperty("sample_id")
    private String sampleId;
    @JsonProperty("analysis_method")
    String analysisMethod;
    @JsonProperty("test_date")
    private LocalDateTime testDate;
    private LimsStatusEnum status;
    private List<LimsResultDto> results;
}