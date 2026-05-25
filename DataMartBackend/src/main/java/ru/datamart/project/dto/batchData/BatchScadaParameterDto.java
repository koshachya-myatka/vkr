package ru.datamart.project.dto.batchData;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchScadaParameterDto {
    private String equipmentId;
    private String parameter;
    private String unit;
    private List<BatchScadaDto> values;
}
