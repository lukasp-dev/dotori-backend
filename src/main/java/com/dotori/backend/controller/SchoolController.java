package com.dotori.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SchoolController {
    @GetMapping("/schools")
    public ResponseEntity<List<Map<String, Object>>> getAllSchools() {
        try {
            InputStream inputStream = new FileInputStream("credentials/demo-schools.json");

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> schools = mapper.readValue(inputStream, new TypeReference<>() {});

            return ResponseEntity.ok(schools);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
