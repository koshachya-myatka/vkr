package ru.datamart.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "fact_lims")
@Getter
@Setter
@ToString
public class LimsEntity {
    @Id
    @Column(name = "record_id")
    private String recordId;
    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private DimBatchEntity batch;
    @Column(name = "sample_id")
    private String sampleId;
    @Column(name = "analysis_method")
    private String analysisMethod;
    @Column(name = "test_date")
    private LocalDateTime testDate;
    @Enumerated(EnumType.ORDINAL)
    private LimsStatusEnum status;
}