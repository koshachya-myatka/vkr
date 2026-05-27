package ru.datamart.project.generators.utils;


import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.models.ScadaStatusEnum;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ScadaDtoGenerator {
    public static final Map<String, double[]> RANGES = Map.of(
            "Температура", new double[]{800, 1200},
            "Давление", new double[]{5, 25},
            "Скорость", new double[]{100, 300},
            "Вибрация", new double[]{0, 10},
            "Влажность", new double[]{20, 80},
            "Напряжение", new double[]{210, 240},
            "Уровень шума", new double[]{30, 90}
    );

    public static ScadaDto generate(String equipmentId, String param, boolean forceFault) {
        ScadaDto dto = new ScadaDto();
        dto.setRecordId("SCADA-" + UUID.randomUUID());
        dto.setSensorId("S-" + ThreadLocalRandom.current().nextInt(1, 100));
        dto.setEquipmentId(equipmentId);
        dto.setParameter(param);
        double value = generateValue(param, forceFault);
        dto.setValue(value);
        dto.setUnit(getUnit(param));
        dto.setStatus(evaluateStatus(param, value));
        dto.setTime(LocalDateTime.now());
        return dto;
    }

    private static double generateValue(String param, boolean forceFault) {
        double[] range = RANGES.get(param);
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        if (forceFault) {
            boolean alarm = rnd.nextBoolean();
            double overMax = alarm
                    ? (range[1] - range[0]) * rnd.nextDouble(0.10, 0.20)
                    : (range[1] - range[0]) * rnd.nextDouble(0.01, 0.10);
            return range[1] + overMax;
        }
        return rnd.nextDouble(range[0], range[1]);
    }

    private static String getUnit(String param) {
        return switch (param) {
            case "Температура" -> "°C";
            case "Давление" -> "Па";
            case "Скорость" -> "об/мин";
            case "Вибрация" -> "мм/с";
            case "Влажность" -> "%";
            case "Напряжение" -> "В";
            case "Уровень шума" -> "дБ";
            default -> "unit";
        };
    }

    private static ScadaStatusEnum evaluateStatus(String param, double value) {
        double[] range = RANGES.get(param);
        if (value >= range[0] && value <= range[1]) return ScadaStatusEnum.NORMAL;
        if (value <= range[1] * 1.1) return ScadaStatusEnum.WARNING;
        return ScadaStatusEnum.ALARM;
    }
}