package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.datamart.project.dto.LastBatchDto;
import ru.datamart.project.services.BatchService;
import ru.datamart.project.services.LimsService;

import java.util.List;

@RestController
@RequestMapping("api/production")
@RequiredArgsConstructor
public class ProductionController {
    private final BatchService batchService;
    private final LimsService limsService;

    @GetMapping("/last-batches")
    public ResponseEntity<List<LastBatchDto>> lastBatches() {
        return ResponseEntity.ok(batchService.getLastBatches());
    }

    @GetMapping("/lims/{batchId}")
    public ResponseEntity<?> getLims(@PathVariable String batchId) {
        return ResponseEntity.ok(limsService.getLimsWithoutResultsByBatchId(batchId));
    }
}
