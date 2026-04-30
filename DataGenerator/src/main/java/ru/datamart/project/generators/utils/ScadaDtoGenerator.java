package ru.datamart.project.generators.utils;


import ru.datamart.project.dto.ScadaDto;
import ru.datamart.project.models.ScadaStatusEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ScadaDtoGenerator {
    private static final Map<String, double[]> RANGES = Map.of(
            "Температура", new double[]{800, 1200},
            "Давление", new double[]{5, 25},
            "Скорость", new double[]{100, 300},
            "Вибрация", new double[]{0, 10},
            "Влажность", new double[]{20, 80},
            "Напряжение", new double[]{210, 240},
            "Уровень шума", new double[]{30, 90}
    );

    public static ScadaDto generate(String equipmentId) {
        ScadaDto dto = new ScadaDto();
        dto.setRecordId("SCADA-" + UUID.randomUUID());
        dto.setSensorId("S-" + ThreadLocalRandom.current().nextInt(1, 100));
        dto.setEquipmentId(equipmentId);
        String param = random();
        dto.setParameter(param);
        double value = generateValue(param);
        dto.setValue(value);
        dto.setUnit(getUnit(param));
        dto.setStatus(evaluateStatus(param, value));
        dto.setTime(LocalDateTime.now());
        return dto;
    }

    private static String random() {
        List<String> keys = new ArrayList<>(RANGES.keySet());
        return keys.get(ThreadLocalRandom.current().nextInt(keys.size()));
    }

    private static double generateValue(String param) {
        double[] range = RANGES.get(param);
        double value = ThreadLocalRandom.current().nextDouble(range[0], range[1]);
        if (ThreadLocalRandom.current().nextDouble() < 0.2) {
            value = range[1] + ThreadLocalRandom.current().nextDouble(1, 15);
        }
        return value;
    }

    private static String getUnit(String param) {
        return switch (param) {
            case "Температура" -> "°C";
            case "Давление" -> "Па";
            case "Скорость" -> "об/мин";
            case "Вибрация" -> "мм/с";
            case "Влажность" -> "%";
            case "Напряжение" -> "В";
            case "Уровень" -> "дБ";
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