package com.dotori.backend.controller;

import com.dotori.backend.dto.UploadInfoRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uploadPersonalInfo")
@RequiredArgsConstructor
public class UploadInfoController {

    @PostMapping
    public ResponseEntity<String> uploadPersonalInfo(@RequestBody UploadInfoRequestDTO request) {
        System.out.println("✅ Received personal info:");
        System.out.println(request);

        return ResponseEntity.ok("{\"message\": \"Upload successful\"}");
    }
}