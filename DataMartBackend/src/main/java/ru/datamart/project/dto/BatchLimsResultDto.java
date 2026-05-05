package ru.datamart.project.dto;

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