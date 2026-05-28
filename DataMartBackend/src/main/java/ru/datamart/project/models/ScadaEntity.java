package ru.datamart.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "fact_scada")
@IdClass(ScadaEntityId.class)
@Getter
@Setter
@ToString
public class ScadaEntity {
    @Id
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "sensor_id")
    private String sensorId;
    @Column(name = "equipment_id")
    private String equipmentId;
    @Id
    @Column(name = "time")
    private LocalDateTime time;
    private String parameter;
    private Double value;
    private String unit;
    @Enumerated(EnumType.STRING)
    private ScadaStatusEnum status;
}