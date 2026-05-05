package ru.datamart.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MetalCardDto {
    private String metalType;
    private String metalTypeName;
    private Long total;
    private Long arrival;
    private Long processing;
    private Long analysis;
    private Long accepted;
    private Long defective;
}