package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.datamart.project.dto.MetalStatisticsCardDto;
import ru.datamart.project.services.BatchService;

import java.util.List;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class ManagementController {
    private final BatchService batchService;

    @GetMapping("/metal-statistics-cards")
    public ResponseEntity<List<MetalStatisticsCardDto>> getMetalStatistics() {
        return ResponseEntity.ok(batchService.getMetalStatisticsCards());
    }
}
