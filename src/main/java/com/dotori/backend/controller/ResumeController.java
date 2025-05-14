package com.dotori.backend.controller;

import com.dotori.backend.service.GCSResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class ResumeController {

    private final GCSResumeService gcsUploadService;

    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file,
                                               @RequestParam("userId") String userId) {
        System.out.println("📥 Controller hit!");
        try {
            String publicUrl = gcsUploadService.uploadFile(file, userId);
            return ResponseEntity.ok(publicUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }
}
