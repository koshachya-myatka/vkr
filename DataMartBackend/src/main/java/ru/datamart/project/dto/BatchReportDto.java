package ru.datamart.project.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchReportDto {
    private BatchDto batch;
    private BatchMesDto mes;
    private List<BatchLimsDto> lims;
    private List<BatchScadaAvgDto> scada;
    private BatchReportInfoDto reportInfo;
}