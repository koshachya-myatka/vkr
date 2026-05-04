package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
