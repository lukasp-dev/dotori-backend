package com.dotori.backend.controller;

import com.dotori.backend.dto.SchoolDTO;
import com.dotori.backend.model.School;
import com.dotori.backend.repository.SchoolRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schools")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SchoolController {
    private final SchoolRepository schoolRepository;

    public SchoolController(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @GetMapping
    public List<SchoolDTO> getAllSchools() {
        List<School> schools = schoolRepository.findAll();
        return schools.stream()
                .map(s -> new SchoolDTO(s.getId(), s.getSchoolName(), s.getRanking(), s.getUrlParameter()))
                .collect(Collectors.toList());
    }
}
