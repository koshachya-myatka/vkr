package ru.datamart.project.generators.utils;


import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.LimsResultDto;
import ru.datamart.project.models.LimsStatusEnum;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LimsDtoGenerator {
    public static List<LimsDto> generate(String batchId, String metalType,
                                         int numberDto, boolean isDefective) {
        List<LimsDto> list = new ArrayList<>();

        Set<Integer> defectIndexes = new HashSet<>();
        if (isDefective && numberDto > 0) {
            int defectCount = Math.min(ThreadLocalRandom.current().nextInt(1, 3), numberDto);
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < numberDto; i++) indexes.add(i);
            Collections.shuffle(indexes);
            defectIndexes.addAll(indexes.subList(0, defectCount));
        }

        for (int i = 0; i < numberDto; i++) {
            boolean forceDefect = defectIndexes.contains(i);
            LimsDto dto = new LimsDto();
            String recordId = "LIMS-" + UUID.randomUUID();
            dto.setRecordId(recordId);
            dto.setBatchId(batchId);
            dto.setSampleId(batchId + "-SAMPLE-" + i);
            dto.setTestDate(LocalDateTime.now());
            String method = MetalType.METHODS.get(ThreadLocalRandom.current().nextInt(MetalType.METHODS.size()));
            dto.setAnalysisMethod(method);
            List<LimsResultDto> results = generateResults(metalType, method, recordId, forceDefect);
            dto.setResults(results);
            dto.setStatus(isDefective ? LimsStatusEnum.REJECTED : LimsStatusEnum.APPROVED);
            list.add(dto);
        }
        return list;
    }

    private static List<LimsResultDto> generateResults(String metalType, String method,
                                                       String recordId, boolean forceDefect) {
        List<LimsResultDto> list = new ArrayList<>();
        switch (method) {
            case "Рентгенофлуоресцентный анализ", "Атомно-эмиссионная спектрометрия" -> {
                Map<String, double[]> parameters = MetalType.XRAY_AND_ATOMIC.get(metalType);
                List<String> paramKeys = new ArrayList<>(parameters.keySet());
                List<Integer> impurityIndexes = new ArrayList<>();
                for (int i = 0; i < paramKeys.size(); i++) {
                    double[] range = parameters.get(paramKeys.get(i));
                    if (range[0] == 0) {
                        impurityIndexes.add(i);
                    }
                }
                Set<Integer> defectParamIndexes = new HashSet<>();
                if (forceDefect && !impurityIndexes.isEmpty()) {
                    Collections.shuffle(impurityIndexes);
                    int defectCount = Math.min(
                            ThreadLocalRandom.current().nextInt(2, 4),
                            impurityIndexes.size()
                    );
                    defectParamIndexes.addAll(impurityIndexes.subList(0, defectCount));
                }
                for (int i = 0; i < paramKeys.size(); i++) {
                    String parameter = paramKeys.get(i);
                    double[] range = parameters.get(parameter);
                    boolean isMainMetal = range[0] > 90;
                    boolean forceThisDefect = !isMainMetal && defectParamIndexes.contains(i);
                    list.add(generateRangeResultDto(
                            parameter, range[0], range[1], "%", recordId, forceThisDefect
                    ));
                }
            }
            case "Испытание на растяжение" -> {
                List<Integer> defectParamIndexes = new ArrayList<>();
                if (forceDefect) {
                    defectParamIndexes.addAll(List.of(0, 1, 2));
                    Collections.shuffle(defectParamIndexes);
                    int defectCount = Math.min(
                            ThreadLocalRandom.current().nextInt(0, 3),
                            defectParamIndexes.size()
                    );
                    defectParamIndexes.subList(0, defectCount);
                }
                list.add(generateMinResultDto("Предел прочности",
                        getMinMax(metalType, method, "Предел прочности", 0),
                        getMinMax(metalType, method, "Предел прочности", 1),
                        "МПа", recordId, defectParamIndexes.contains(0)));
                list.add(generateMinResultDto("Предел текучести",
                        getMinMax(metalType, method, "Предел текучести", 0),
                        getMinMax(metalType, method, "Предел текучести", 1),
                        "МПа", recordId, defectParamIndexes.contains(1)));
                list.add(generateMinResultDto("Относительное удлинение",
                        getMinMax(metalType, method, "Относительное удлинение", 0),
                        getMinMax(metalType, method, "Относительное удлинение", 1),
                        "%", recordId, defectParamIndexes.contains(2)));
            }
            case "Испытание на твёрдость" -> {
                List<Integer> defectParamIndexes = new ArrayList<>();
                if (forceDefect) {
                    defectParamIndexes.addAll(List.of(0, 1));
                    Collections.shuffle(defectParamIndexes);
                    int defectCount = Math.min(
                            ThreadLocalRandom.current().nextInt(0, 2),
                            defectParamIndexes.size()
                    );
                    defectParamIndexes.subList(0, defectCount);
                }
                list.add(generateRangeResultDto("Твёрдость по Бринеллю",
                        getMinMax(metalType, method, "Твёрдость по Бринеллю", 0),
                        getMinMax(metalType, method, "Твёрдость по Бринеллю", 1),
                        "HB", recordId, defectParamIndexes.contains(0)));
                list.add(generateRangeResultDto("Твёрдость по Виккерсу",
                        getMinMax(metalType, method, "Твёрдость по Виккерсу", 0),
                        getMinMax(metalType, method, "Твёрдость по Виккерсу", 1),
                        "HV", recordId, defectParamIndexes.contains(1)));
            }
            case "Испытание на ударную вязкость" -> {
                list.add(generateMinResultDto("Ударная вязкость по Шарпи",
                        getMinMax(metalType, method, "Ударная вязкость по Шарпи", 0),
                        getMinMax(metalType, method, "Ударная вязкость по Шарпи", 1),
                        "Дж/см2", recordId, forceDefect));
            }
            case "Металлографический анализ" -> {
                List<Integer> defectParamIndexes = new ArrayList<>();
                if (forceDefect) {
                    defectParamIndexes.addAll(List.of(0, 1, 2));
                    Collections.shuffle(defectParamIndexes);
                    int defectCount = Math.min(
                            ThreadLocalRandom.current().nextInt(0, 3),
                            defectParamIndexes.size()
                    );
                    defectParamIndexes.subList(0, defectCount);
                }
                list.add(generateStringParamResultDto("Наличие дефектов",
                        List.of("да", "нет"), "нет",
                        recordId, defectParamIndexes.contains(0)));
                list.add(generateStringParamResultDto("Однородность",
                        List.of("высокая", "средняя", "низкая"), "высокая",
                        recordId, defectParamIndexes.contains(1)));
                list.add(generateRangeResultDto("Размер зерна",
                        getMinMax(metalType, method, "Размер зерна", 0),
                        getMinMax(metalType, method, "Размер зерна", 1),
                        "мкм", recordId, defectParamIndexes.contains(2)));
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
                                                        String unit, String recordId,
                                                        boolean forceDefect) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        double value;
        boolean normal;
        if (forceDefect) {
            double deviation = (max > 0)
                    ? rnd.nextDouble(max * 0.10, max * 0.50)
                    : rnd.nextDouble(0.001, 0.01);
            value = max + deviation;
            normal = false;
        } else {
            value = (max > min) ? rnd.nextDouble(min, max) : min;
            normal = true;
        }
        return build(recordId, name, value, unit, normal);
    }

    private static LimsResultDto generateMinResultDto(String name, double min, double max,
                                                      String unit, String recordId,
                                                      boolean forceDefect) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        double value;
        boolean normal;
        if (forceDefect) {
            value = min - rnd.nextDouble((max - min) * 0.01, (max - min) * 0.25);
            normal = false;
        } else {
            value = rnd.nextDouble(min, max);
            normal = true;
        }
        return build(recordId, name, value, unit, normal);
    }

    private static LimsResultDto generateStringParamResultDto(String name, List<String> values,
                                                              String normalValue, String recordId,
                                                              boolean forceDefect) {
        String value;
        if (forceDefect) {
            List<String> abnormal = values.stream()
                    .filter(v -> !v.equals(normalValue))
                    .toList();
            value = abnormal.get(ThreadLocalRandom.current().nextInt(abnormal.size()));
        } else {
            value = normalValue;
        }
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