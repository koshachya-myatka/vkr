package ru.datamart.project.dto;

public interface MetalCardProjection {
    String getMetalType();
    Long getTotal();
    Long getArrival();
    Long getProcessing();
    Long getAnalysis();
    Long getAccepted();
    Long getDefective();
}