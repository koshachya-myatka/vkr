package ru.datamart.project.generators.utils;

import ru.datamart.project.dto.MesDto;
import ru.datamart.project.models.MesProcessStatusEnum;
import ru.datamart.project.models.MesStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class MesDtoGenerator {
    public static MesDto generate(String batchId, String equipmentId, String metalType) {
        MesDto dto = new MesDto();
        dto.setRecordId("MES-" + UUID.randomUUID());
        dto.setBatchId(batchId);
        dto.setEquipmentId(equipmentId);
        dto.setStartTime(LocalDateTime.now());
        dto.setMetalType(metalType);
        dto.setProcessStatus(MesProcessStatusEnum.ARRIVAL);
        dto.setOperatorId("OP-" + ThreadLocalRandom.current().nextInt(1, 300));
        return dto;
    }

    public static MesDto startProcessing(MesDto dto) {
        dto.setProcessStatus(MesProcessStatusEnum.PROCESSING);
        dto.setProcessingTime(LocalDateTime.now());
        double temperature = randomWithoutOrWithDeviation(900D, 1100D, false);
        double pressure = randomWithoutOrWithDeviation(10D, 20D, false);
        double duration = randomWithoutOrWithDeviation(100D, 500D, false);
        double energy = randomWithoutOrWithDeviation(100D, 500D, false);
        dto.setTemperature(temperature);
        dto.setPressure(pressure);
        dto.setDurationSec((int) duration);
        dto.setEnergyConsumption(energy);
        MesStatusEnum status = calculateStatus(temperature, pressure, energy);
        dto.setStatus(status);
        return dto;
    }

    private static double randomWithoutOrWithDeviation(Double min, Double max, boolean fix) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        double value = rnd.nextDouble(min, max);
        if (fix) {
            return value;
        }
        if (rnd.nextDouble() < 0.01) {
            value = max + rnd.nextDouble((max - min) * 0.01, (max - min) * 0.15);
        }
        return value;
    }

    private static MesStatusEnum calculateStatus(double temperature, double pressure, double energy) {
        int deviations = 0;
        if (temperature > 1100 || temperature < 900) deviations++;
        if (pressure > 20 || pressure < 10) deviations++;
        if (energy > 500 || energy < 100) deviations++;
        if (deviations == 0) return MesStatusEnum.NORMAL;
        if (deviations == 1) return MesStatusEnum.WARNING;
        return MesStatusEnum.ALARM;
    }

    public static void scheduleFixParameters(MesDto dto, Consumer<MesDto> sender) {
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Не удалось запустить исправление параметров MES", e);
            }
            double temperature = randomWithoutOrWithDeviation(900D, 1100D, true);
            double pressure = randomWithoutOrWithDeviation(10D, 20D, true);
            double energy = randomWithoutOrWithDeviation(100D, 500D, true);
            dto.setTemperature(temperature);
            dto.setPressure(pressure);
            dto.setEnergyConsumption(energy);
            dto.setStatus(calculateStatus(temperature, pressure, energy));
            sender.accept(dto);
        }).start();
    }
}