package ru.datamart.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MetalCardDto {
    private String metalType;
    private Long total;
    private Long arrival;
    private Long processing;
    private Long analysis;
    private Long accepted;
    private Long defective;
}