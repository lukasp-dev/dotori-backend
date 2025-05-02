package com.dotori.backend.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
public class GCSResumeService {
    private Storage storage = StorageOptions.getDefaultInstance().getService();
    private final String BUCKET = "dotori-public-assets-resume";
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
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

