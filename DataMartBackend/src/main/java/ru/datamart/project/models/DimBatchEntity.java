package ru.datamart.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "dim_batch")
@Getter
@Setter
@ToString
public class DimBatchEntity {
    @Id
    @Column(name = "batch_id")
    private String batchId;
    @Column(name = "metal_type")
    private String metalType;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "process_status")
    @Enumerated(EnumType.ORDINAL)
    private MesProcessStatusEnum processStatus;
    @Column(name = "output_yield")
    private Double outputYield;
}