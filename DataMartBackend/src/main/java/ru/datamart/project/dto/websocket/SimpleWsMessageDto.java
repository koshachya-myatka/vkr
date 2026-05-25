package ru.datamart.project.dto.websocket;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SimpleWsMessageDto {
    private String message;
}
