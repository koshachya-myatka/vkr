package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.datamart.project.dto.*;
import ru.datamart.project.services.BatchService;
import ru.datamart.project.services.LimsService;
import ru.datamart.project.services.MesService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MetalController {
    private final BatchService batchService;
    private final MesService mesService;
    private final LimsService limsService;

    @GetMapping("/metal-cards")
    public ResponseEntity<List<MetalCardDto>> metalCards() {
        return ResponseEntity.ok(batchService.getMetalCards());
    }

    @PostMapping("/metals")
    public ResponseEntity<List<MetalBatchDto>> metalBatches(@RequestBody MetalBatchFilterDto dto) {
        return ResponseEntity.ok(batchService.getMetalBatches(dto));
    }

    @GetMapping("/batches/{batchId}")
    public ResponseEntity<?> batch(@PathVariable String batchId) {
        BatchDto dto = batchService.getBatchById(batchId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/mes/{batchId}")
    public ResponseEntity<?> batchMes(@PathVariable String batchId) {
        BatchMesDto dto = mesService.getMesByBatchId(batchId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lims/{batchId}")
    public ResponseEntity<?> getLimsWithoutResults(@PathVariable String batchId) {
        return ResponseEntity.ok(limsService.getLimsWithoutResultsByBatchId(batchId));
    }
}