package ru.datamart.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fact_mes")
@Getter
@Setter
public class MesEntity {
    @Id
    @Column(name = "record_id")
    private String recordId;
    @OneToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DimBatchEntity batch;
    @Column(name = "equipment_id")
    private String equipmentId;
    @Column(name = "operator_id")
    private String operatorId;
    private Double temperature;
    private Double pressure;
    @Column(name = "duration_sec")
    private Integer durationSec;
    @Column(name = "energy_consumption")
    private Double energyConsumption;
    private String additives;
    private MesStatusEnum status;
}