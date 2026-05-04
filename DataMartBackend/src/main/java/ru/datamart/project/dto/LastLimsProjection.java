package ru.datamart.project.dto;

import java.time.LocalDateTime;

public interface LastLimsProjection {
    String getSampleId();
    String getMetalType();
    String getAnalysisMethod();
    LocalDateTime getTestDate();
    String getStatus();
}
