package com.dotori.backend.controller;

import com.dotori.backend.dto.EssayResponseDTO;
import com.dotori.backend.service.EssayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/essays")
@RequiredArgsConstructor
public class EssayController {
    private final EssayService essayService;

    @GetMapping("/{schoolId}")
    public EssayResponseDTO getEssayBySchoolId(@PathVariable Integer schoolId) {
        return essayService.getEssayBySchoolId(schoolId);
    }
}

