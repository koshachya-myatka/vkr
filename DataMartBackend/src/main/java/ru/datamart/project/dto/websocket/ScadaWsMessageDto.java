package ru.datamart.project.dto.websocket;

import lombok.*;
import ru.datamart.project.dto.batchData.BatchScadaDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScadaWsMessageDto {
    private String batchId;
    private BatchScadaDto scada;
}