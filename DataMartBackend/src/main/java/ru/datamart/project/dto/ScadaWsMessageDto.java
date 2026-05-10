package ru.datamart.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScadaWsMessageDto {
    private String batchId;
    private BatchScadaDto scada;
}