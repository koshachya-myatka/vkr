package ru.datamart.project.dto;

import java.time.LocalDateTime;

public interface LastBatchProjection {
    String getBatchId();
    String getMetalType();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    String getProcessStatus();
}