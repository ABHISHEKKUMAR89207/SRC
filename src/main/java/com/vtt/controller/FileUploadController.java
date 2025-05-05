package com.vtt.controller;



import com.vtt.FileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file-upload")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${file.base-url}")
    private String fileBaseUrl;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }

            String fileName = fileStorageService.storeFile(file);
            String fileUrl = fileBaseUrl + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("fileUrl", fileUrl);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.error("Error while uploading file: ", e);
            return ResponseEntity.status(500).body("Error uploading file");
        } catch (Exception e) {
            logger.error("Unexpected error while uploading file: ", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
