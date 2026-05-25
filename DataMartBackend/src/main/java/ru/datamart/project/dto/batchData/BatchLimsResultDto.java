package ru.datamart.project.dto.batchData;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchLimsResultDto {
    private String parameterName;
    private String value;
    private String unit;
    private Boolean normal;
}