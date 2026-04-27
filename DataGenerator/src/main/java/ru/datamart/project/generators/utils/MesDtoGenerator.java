package ru.datamart.project.generators.utils;

import ru.datamart.project.dto.MesDto;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MesDtoGenerator {
    public static MesDto generate(String batchId, String equipmentId, String metalType) {
        MesDto dto = new MesDto();
        dto.setRecordId("MES" + UUID.randomUUID());
        dto.setBatchId(batchId);
        dto.setEquipmentId(equipmentId);
        dto.setStartTime(LocalDateTime.now());
        dto.setMetalType(metalType);
        dto.setProcessStatus(0);
        dto.setOperatorId("OP-" + ThreadLocalRandom.current().nextInt(1, 300));
        return dto;
    }

    public static MesDto startProcessing(MesDto dto) {
        dto.setProcessStatus(1);
        //todo поправить
        dto.setTemperature(randomWithDeviation(900D, 1100D));
        dto.setPressure(randomWithDeviation(10D, 20D));
        dto.setDurationSec((int) randomWithDeviation(100D, 500D));
        dto.setEnergyConsumption(randomWithDeviation(100D, 500D));
        //todo придумать
        dto.setAdditives("mewmew");
        //todo решить со статусом вопрос
        return dto;
    }

    private static double randomWithDeviation(Double min, Double max) {
        double value = ThreadLocalRandom.current().nextDouble(min, max);
        if (ThreadLocalRandom.current().nextDouble() < 0.2) {
            value = max + ThreadLocalRandom.current().nextDouble(1, 15);
        }
        return value;
    }
}