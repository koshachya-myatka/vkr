package ru.datamart.project.generators.utils;

import ru.datamart.project.dto.MesDto;
import ru.datamart.project.models.MesProcessStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MesDtoGenerator {
    public static MesDto generate(String batchId, String equipmentId, String metalType) {
        MesDto dto = new MesDto();
        dto.setRecordId("MES-" + UUID.randomUUID());
        dto.setOrderId("ORDER-" + UUID.randomUUID());
        dto.setBatchId(batchId);
        dto.setEquipmentId(equipmentId);
        dto.setOperatorId("OP-" + ThreadLocalRandom.current().nextInt(1, 300));
        dto.setStartTime(LocalDateTime.now());
        dto.setMetalType(metalType);
        dto.setProcessStatus(MesProcessStatusEnum.ARRIVAL);
        return dto;
    }

    public static MesDto startProcessing(MesDto dto) {
        dto.setProcessStatus(MesProcessStatusEnum.PROCESSING);
        dto.setProcessingTime(LocalDateTime.now());
        double chargeMass = generateValue(1D, 100D);
        dto.setChargeMass(chargeMass);
        return dto;
    }

    public static MesDto startAnalyses(MesDto dto, boolean isDefective) {
        dto.setProcessStatus(MesProcessStatusEnum.ANALYSIS);
        dto.setAnalysesTime(LocalDateTime.now());
        double chargeMass = dto.getChargeMass();
        double minValue = isDefective ? chargeMass * 0.5 : chargeMass * 0.75;
        double outputMass = generateValue(minValue, chargeMass * 0.95);
        double outputYield = outputMass / chargeMass * 100;
        double duration = generateValue(100D, 650D);
        dto.setDurationMin((int) duration);
        dto.setOutputMass(outputMass);
        dto.setOutputYield(outputYield);
        return dto;
    }

    private static double generateValue(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}