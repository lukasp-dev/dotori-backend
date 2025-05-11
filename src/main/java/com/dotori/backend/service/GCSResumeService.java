package com.dotori.backend.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
public class GCSResumeService {
    private final Storage storage;
    @Value("${resume.bucket.name}")
    private String BUCKET;

    public GCSResumeService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadFile(MultipartFile file, String userId) throws IOException {
        String fileName = String.format("resumes/%s/%s_%s", userId, UUID.randomUUID(), file.getOriginalFilename());
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
                if (attempt >= maxRetries) {
                    throw new IOException("Failed to upload file after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    
        return String.format("https://storage.googleapis.com/%s/%s", BUCKET, fileName);
    }    
}

