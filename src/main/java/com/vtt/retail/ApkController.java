package com.vtt.retail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ApkController {

    @Value("${apk.file.path}")
    private String apkPath;

    @Value("${apk.file.name}")
    private String apkName;

    @GetMapping("/api/download/apk")
    public ResponseEntity<Resource> downloadApk() {
        try {
            Path filePath = Paths.get(apkPath).resolve(apkName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + filePath);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + apkName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.android.package-archive")
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Error downloading APK: " + e.getMessage());
        }
    }
}