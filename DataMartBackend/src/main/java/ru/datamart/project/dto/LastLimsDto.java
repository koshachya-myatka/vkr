package ru.datamart.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LastLimsDto {
    private String sampleId;
    private String metalType;
    String analysisMethod;
    private LocalDateTime testDate;
    private String status;
    private String statusName;
}