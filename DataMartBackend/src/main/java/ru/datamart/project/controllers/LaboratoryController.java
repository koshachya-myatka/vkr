package ru.datamart.project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.datamart.project.dto.LastLimsDto;
import ru.datamart.project.services.LimsService;

import java.util.List;

@RestController
@RequestMapping("/api/laboratory")
@RequiredArgsConstructor
public class LaboratoryController {
    private final LimsService limsService;

    @GetMapping("/last-lims")
    public List<LastLimsDto> lastLims() {
        return limsService.getLastLims();
    }
}