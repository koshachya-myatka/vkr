package ru.datamart.project.generators.utils;


import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.LimsResultDto;
import ru.datamart.project.models.LimsStatusEnum;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LimsDtoGenerator {
    public static List<LimsDto> generate(String batchId, String metalType, int numberDto) {
        List<LimsDto> list = new ArrayList<>();
        for (int i = 0; i < numberDto; i++) {
            LimsDto dto = new LimsDto();
            String recordId = "LIMS-" + UUID.randomUUID();
            dto.setRecordId(recordId);
            dto.setBatchId(batchId);
            dto.setSampleId(batchId + "-SAMPLE-" + i);
            dto.setTestDate(LocalDateTime.now());
            String method = MetalType.METHODS.get(ThreadLocalRandom.current().nextInt(MetalType.METHODS.size()));
            dto.setAnalysisMethod(method);
            List<LimsResultDto> results = generateResults(metalType, method, recordId);
            dto.setResults(results);
            long defects = results.stream().filter(r -> !r.getNormal()).count();
            if (defects == 0) {
                dto.setStatus(LimsStatusEnum.APPROVED);
            } else {
                dto.setStatus(LimsStatusEnum.REJECTED);
            }
            list.add(dto);
        }
        return list;
    }

    private static List<LimsResultDto> generateResults(String metalType, String method, String recordId) {
        List<LimsResultDto> list = new ArrayList<>();
        switch (method) {
            case "Рентгенофлуоресцентный анализ", "Атомно-эмиссионная спектрометрия" -> {
                Map<String, double[]> parameters = MetalType.XRAY_AND_ATOMIC.get(metalType);
                for (String parameter : parameters.keySet()) {
                    list.add(generateRangeResultDto(parameter,
                            getMinMax(metalType, method, parameter, 0),
                            getMinMax(metalType, method, parameter, 1),
                            "%", recordId));
                }
            }
            case "Испытание на растяжение" -> {
                list.add(generateMinResultDto("Предел прочности",
                        getMinMax(metalType, method, "Предел прочности", 0),
                        getMinMax(metalType, method, "Предел прочности", 1),
                        "МПа", recordId));
                list.add(generateMinResultDto("Предел текучести",
                        getMinMax(metalType, method, "Предел текучести", 0),
                        getMinMax(metalType, method, "Предел текучести", 1),
                        "МПа", recordId));
                list.add(generateMinResultDto("Относительное удлинение",
                        getMinMax(metalType, method, "Относительное удлинение", 0),
                        getMinMax(metalType, method, "Относительное удлинение", 1),
                        "%", recordId));
            }
            case "Испытание на твёрдость" -> {
                list.add(generateRangeResultDto("Твёрдость по Бринеллю",
                        getMinMax(metalType, method, "Твёрдость по Бринеллю", 0),
                        getMinMax(metalType, method, "Твёрдость по Бринеллю", 1),
                        "HB", recordId));
                list.add(generateRangeResultDto("Твёрдость по Виккерсу",
                        getMinMax(metalType, method, "Твёрдость по Виккерсу", 0),
                        getMinMax(metalType, method, "Твёрдость по Виккерсу", 1),
                        "HV", recordId));
            }
            case "Испытание на ударную вязкость" -> {
                list.add(generateMinResultDto("Ударная вязкость по Шарпи",
                        getMinMax(metalType, method, "Ударная вязкость по Шарпи", 0),
                        getMinMax(metalType, method, "Ударная вязкость по Шарпи", 1),
                        "Дж/см2", recordId));
            }
            case "Металлографический анализ" -> {
                list.add(generateStringParamResultDto("Наличие дефектов",
                        List.of("да", "нет"), "нет", recordId));
                list.add(generateStringParamResultDto("Однородность",
                        List.of("высокая", "средняя", "низкая"), "высокая", recordId));
                list.add(generateRangeResultDto("Размер зерна",
                        getMinMax(metalType, method, "Размер зерна", 0),
                        getMinMax(metalType, method, "Размер зерна", 1),
                        "мкм", recordId));
            }
        }
        return list;
    }

    private static Double getMinMax(String metalType, String method, String parameter, int index) {
        double value = 0;
        switch (method) {
            case "Рентгенофлуоресцентный анализ", "Атомно-эмиссионная спектрометрия" -> {
                double[] values = Arrays.stream(MetalType.XRAY_AND_ATOMIC.get(metalType).get(parameter)).toArray();
                value = values[index];
            }
            case "Испытание на растяжение" -> {
                double[] values = Arrays.stream(MetalType.STRETCHING.get(metalType).get(parameter)).toArray();
                value = values[index];
            }
            case "Испытание на твёрдость" -> {
                double[] values = Arrays.stream(MetalType.HARDNESS.get(metalType).get(parameter)).toArray();
                value = values[index];
            }
            case "Испытание на ударную вязкость" -> {
                double[] values = Arrays.stream(MetalType.IMPACT_STRENGTH.get(metalType).get(parameter)).toArray();
                value = values[index];
            }
            case "Металлографический анализ" -> {
                double[] values = Arrays.stream(MetalType.METALLOGRAPHIC.get(metalType).get(parameter)).toArray();
                value = values[index];
            }
        }
        return value;
    }

    private static LimsResultDto generateRangeResultDto(String name, double min, double max,
                                                        String unit, String recordId) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        double value = (max > min) ? rnd.nextDouble(min, max) : min;
        boolean normal = true;
        if (rnd.nextDouble() < 0.005) {
            value = max + rnd.nextDouble((max - min) * 0.01, (max - min) * 0.15);
            normal = false;
        }
        return build(recordId, name, value, unit, normal);
    }

    private static LimsResultDto generateRangeMetalTypeResultDto(String name, double min, double max,
                                                                 String unit, String recordId,
                                                                 List<LimsResultDto> list) {
        double sumOtherElements = list.stream()
                .map(v -> Double.parseDouble(v.getValue()))
                .reduce(Double::sum).orElseGet(() -> 0D);
        double value = 100 - sumOtherElements;
        boolean normal = value >= min;
        return build(recordId, name, value, unit, normal);
    }

    private static LimsResultDto generateMinResultDto(String name, double min, double max,
                                                      String unit, String recordId) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        double value = rnd.nextDouble(min, max);
        boolean normal = true;
        if (rnd.nextDouble() < 0.005) {
            value = min - rnd.nextDouble((max - min) * 0.01, (max - min) * 0.25);
            normal = false;
        }
        return build(recordId, name, value, unit, normal);
    }

    private static LimsResultDto generateStringParamResultDto(String name, List<String> values, String normalValue, String recordId) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        String value = values.get(rnd.nextInt(values.size()));
        boolean normal = value.equals(normalValue);
        return build(recordId, name, value, "", normal);
    }

    private static LimsResultDto build(String recordId, String name, Object value, String unit, boolean normal) {
        LimsResultDto dto = new LimsResultDto();
        dto.setRecordId(recordId);
        dto.setParameterName(name);
        dto.setValue(value.toString());
        dto.setUnit(unit);
        dto.setNormal(normal);
        return dto;
    }
}