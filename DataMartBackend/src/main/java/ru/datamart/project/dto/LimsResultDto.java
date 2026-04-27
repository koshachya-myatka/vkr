package ru.datamart.project.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LimsResultDto {
    String record_id;
    String parameter_name;
    Float value;
    String unit;
    Boolean normal;
}