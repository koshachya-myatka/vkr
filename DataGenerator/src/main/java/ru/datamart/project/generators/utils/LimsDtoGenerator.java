package ru.datamart.project.generators.utils;


import ru.datamart.project.dto.LimsDto;
import ru.datamart.project.dto.LimsResultDto;
import ru.datamart.project.models.LimsStatusEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class LimsDtoGenerator {
    private final static List<String> METHODS = List.of("Рентгенофлуоресцентный анализ", "Атомно-эмиссионная спектрометрия",
            "Испытание на растяжение", "Испытание на твёрдость", "Испытание на ударную вязкость", "Металлографический анализ");

    public static List<LimsDto> generate(String batchId, String metalType, int numberDto) {
        List<LimsDto> list = new ArrayList<>();
        for (int i = 0; i < numberDto; i++) {
            LimsDto dto = new LimsDto();
            String recordId = "LIMS-" + UUID.randomUUID();
            dto.setRecordId(recordId);
            dto.setBatchId(batchId);
            dto.setSampleId(batchId + "-SAMPLE-" + i);
            dto.setTestDate(LocalDateTime.now());
            String method = METHODS.get(ThreadLocalRandom.current().nextInt(METHODS.size()));
            dto.setAnalysisMethod(method);

            List<LimsResultDto> results = generateResults(metalType, recordId);
            dto.setResults(results);
            long defects = results.stream().filter(r -> !r.getNormal()).count();
            if (defects < 2) {
                dto.setStatus(LimsStatusEnum.APPROVED);
            } else {
                dto.setStatus(LimsStatusEnum.REJECTED);
            }
            list.add(dto);
        }
        return list;
    }

    private static List<LimsResultDto> generateResults(String metalType, String analysisRecordId) {
        List<LimsResultDto> list = new ArrayList<>();
        for (int i = 0; i <; i++) {
            LimsResultDto dto = new LimsResultDto();
            dto.setRecordId(analysisRecordId);
            dto.setParameterName();
            dto.setValue();
            dto.setUnit();
            dto.setNormal();
            list.add(dto);
        }
        return list;
    }
}