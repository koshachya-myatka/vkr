package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.datamart.project.dto.LastBatchDto;
import ru.datamart.project.services.BatchService;

import java.util.List;

@RestController
@RequestMapping("api/production")
@RequiredArgsConstructor
public class ProductionController {
    private final BatchService batchService;

    @GetMapping("/last-batches")
    public List<LastBatchDto> lastBatches() {
        return batchService.getLastBatches();
    }
}
