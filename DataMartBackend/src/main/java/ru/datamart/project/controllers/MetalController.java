package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.datamart.project.dto.MetalBatchDto;
import ru.datamart.project.dto.MetalBatchFilterDto;
import ru.datamart.project.dto.MetalCardDto;
import ru.datamart.project.services.BatchService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MetalController {
    private final BatchService batchService;

    @GetMapping("/metal-cards")
    public List<MetalCardDto> metalCards() {
        return batchService.getMetalCards();
    }

    @GetMapping("/metals")
    public List<MetalBatchDto> metalBatches(@RequestBody MetalBatchFilterDto dto) {
        return batchService.getMetalBatches(dto);
    }
}