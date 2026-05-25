package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.datamart.project.dto.dashboards.LastLimsDto;
import ru.datamart.project.services.LimsService;

import java.util.List;

@RestController
@RequestMapping("/api/laboratory")
@RequiredArgsConstructor
public class LaboratoryController {
    private final LimsService limsService;

    @GetMapping("/last-lims")
    public ResponseEntity<List<LastLimsDto>> lastLims() {
        return ResponseEntity.ok(limsService.getLastLims());
    }

    @GetMapping("/lims/{batchId}")
    public ResponseEntity<?> getLims(@PathVariable String batchId) {
        return ResponseEntity.ok(limsService.getLimsByBatchId(batchId));
    }
}