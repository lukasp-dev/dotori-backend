package com.dotori.backend.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GCSResumeService {

    private final Storage storage;

    @Value("${resume.bucket.name}")
    private String BUCKET;

    public String uploadFile(MultipartFile file, String userId) throws IOException {
        String fileName = String.format("resumes/%s/%s_%s", userId, UUID.randomUUID(), file.getOriginalFilename());
        System.out.println("📤 Uploading to GCS...");
        System.out.println("📎 File: " + fileName);
        System.out.println("👤 User ID: " + userId);
        System.out.println("🪣 Bucket: " + BUCKET);

        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET, fileName)
                .setContentType(file.getContentType())
                .build();

        int maxRetries = 3;
        int attempt = 0;
        boolean success = false;

        while (attempt < maxRetries && !success) {
            try {
                storage.create(blobInfo, file.getBytes());
                success = true;
            } catch (IOException e) {
                attempt++;
                System.out.println("⚠️ Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt >= maxRetries) {
                    throw new IOException("❌ Failed to upload file after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Thread interrupted during retry", ie);
                }
            }
        }

        String publicUrl = String.format("https://storage.googleapis.com/%s/%s", BUCKET, fileName);
        System.out.println("✅ Upload successful: " + publicUrl);
        return publicUrl;
    }
}
