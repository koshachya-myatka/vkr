package ru.datamart.project.dto.report;

import lombok.*;
import ru.datamart.project.dto.batchData.BatchDto;
import ru.datamart.project.dto.batchData.BatchLimsDto;
import ru.datamart.project.dto.batchData.BatchMesDto;
import ru.datamart.project.dto.batchData.BatchScadaAvgDto;

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