package ru.datamart.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "fact_mes")
@Getter
@Setter
@ToString
public class MesEntity {
    @Id
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "order_id")
    private String orderId;
    @OneToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DimBatchEntity batch;
    @Column(name = "equipment_id")
    private String equipmentId;
    @Column(name = "operator_id")
    private String operatorId;
    @Column(name = "charge_mass")
    private Double chargeMass;
    @Column(name = "output_mass")
    private Double outputMass;
    @Column(name = "duration_min")
    private Integer durationMin;
}