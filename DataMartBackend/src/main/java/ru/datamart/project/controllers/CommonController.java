package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.datamart.project.dto.*;
import ru.datamart.project.services.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommonController {
    private final BatchService batchService;
    private final MesService mesService;
    private final LimsService limsService;
    private final UserProfileService userProfileService;
    private final ReportService reportService;
    private final ScadaService scadaService;

    @GetMapping("/metal-cards")
    public ResponseEntity<List<MetalCardDto>> metalCards() {
        return ResponseEntity.ok(batchService.getMetalCards());
    }

    @PostMapping("/metals")
    public ResponseEntity<PageResponseDto<MetalBatchDto>> metalBatches(@RequestBody MetalBatchFilterDto dto) {
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

    @GetMapping("/scada/{batchId}")
    public ResponseEntity<List<BatchScadaAvgDto>> getScadaAvgByBatchId(@PathVariable String batchId) {
        return ResponseEntity.ok(scadaService.getScadaAvgByBatchId(batchId));
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserProfileDto> getCurrentUser() {
        return ResponseEntity.ok(userProfileService.getCurrentUser());
    }

    @GetMapping("/report/{batchId}")
    public ResponseEntity<?> createReport(@PathVariable String batchId) {
        byte[] pdfBytes = reportService.generateReport(batchId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline()
                .filename("report-" + batchId + ".pdf")
                .build()
        );
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}