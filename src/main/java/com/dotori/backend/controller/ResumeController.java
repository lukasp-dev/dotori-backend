package com.dotori.backend.controller;

import com.dotori.backend.service.GCSResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor

public class ResumeController {
    private final GCSResumeService gcsUploadService;

    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
        try {
            String publicUrl = gcsUploadService.uploadFile(file, userId);
            return ResponseEntity.ok(publicUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }
}

