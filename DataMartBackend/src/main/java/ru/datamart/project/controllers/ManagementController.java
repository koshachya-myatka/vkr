package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.datamart.project.dto.BatchScadaAvgDto;
import ru.datamart.project.dto.MetalStatisticsCardDto;
import ru.datamart.project.services.BatchService;
import ru.datamart.project.services.ScadaService;

import java.util.List;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class ManagementController {
    private final BatchService batchService;
    private final ScadaService scadaService;

    @GetMapping("/metal-statistics-cards")
    public ResponseEntity<List<MetalStatisticsCardDto>> getMetalStatistics() {
        return ResponseEntity.ok(batchService.getMetalStatisticsCards());
    }

    @GetMapping("/scada/{batchId}")
    public ResponseEntity<List<BatchScadaAvgDto>> getScada(@PathVariable String batchId) {
        return ResponseEntity.ok(scadaService.getScadaAvgByBatchId(batchId));
    }
}
