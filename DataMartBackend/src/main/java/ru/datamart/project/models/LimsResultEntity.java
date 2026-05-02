package ru.datamart.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "fact_lims_results")
@Getter
@Setter

@ToString
public class LimsResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "record_id", nullable = false)
    private LimsEntity lims;
    @Column(name = "parameter_name")
    private String parameterName;
    private String value;
    private String unit;
    private Boolean normal;
}