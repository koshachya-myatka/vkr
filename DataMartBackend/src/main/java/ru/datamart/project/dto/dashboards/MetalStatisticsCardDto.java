package ru.datamart.project.dto.dashboards;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MetalStatisticsCardDto {
    private String metalType;
    private String metalTypeName;
    private Long batchesCount;
    private Double averageOutputYield;
    private Double defectivePercent;
}
