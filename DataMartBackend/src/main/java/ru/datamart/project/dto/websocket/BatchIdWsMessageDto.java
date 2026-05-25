package ru.datamart.project.dto.websocket;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BatchIdWsMessageDto {
    private String message;
    private String batchId;
}
