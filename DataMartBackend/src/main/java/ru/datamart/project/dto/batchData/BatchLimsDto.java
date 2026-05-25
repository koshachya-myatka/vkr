package ru.datamart.project.dto.batchData;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchLimsDto {
    private String recordId;
    private String sampleId;
    private String analysisMethod;
    private LocalDateTime testDate;
    private String status;
    private String statusName;
    private List<BatchLimsResultDto> results;
}
