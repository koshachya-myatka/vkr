package ru.datamart.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchReportInfoDto {
    private long alarmCount;
    private long deviationCount;
    private String author;
    private LocalDateTime createdAt;
}