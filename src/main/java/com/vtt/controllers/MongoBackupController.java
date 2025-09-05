package com.vtt.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/backup")
public class MongoBackupController {

    private static final String DATABASE_NAME = "SRC";  // MongoDB database name
    private static final String BACKUP_FOLDER = "backup"; // Local folder to save dump

    @GetMapping("/download")
    public ResponseEntity<?> downloadBackup() {
        try {
            // Ensure backup folder exists
            File backupDir = new File(BACKUP_FOLDER);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            // Step 1: Run mongodump command
            ProcessBuilder pb = new ProcessBuilder(
                    "/usr/bin/mongodump",
                    "--db", DATABASE_NAME,
                    "--out", BACKUP_FOLDER
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Wait until dump finishes
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create backup. Exit code: " + exitCode);
            }

            // Step 2: Zip the backup folder
            String zipFileName = DATABASE_NAME + "_backup.zip";
            File zipFile = new File(zipFileName);
            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                zipDirectory(backupDir, backupDir.getName(), zos);
            }

            // Step 3: Return the zip file as response
            byte[] fileContent = Files.readAllBytes(zipFile.toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", zipFileName);

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating backup: " + e.getMessage());
        }
    }

    private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }
            try (FileInputStream fis = new FileInputStream(file)) {
                String zipEntryName = parentFolder + "/" + file.getName();
                zos.putNextEntry(new ZipEntry(zipEntryName));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            }
        }
    }
}
