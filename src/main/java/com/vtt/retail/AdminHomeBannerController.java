package com.vtt.retail;

import com.vtt.FileStorage.FileStorageService;
import com.vtt.retail.entities.HomeBanner;
import com.vtt.retail.repository.HomeBannerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/retail/banners")
@Tag(name = "Admin Home Banner Controller", description = "Admin API for managing homepage carousel banners")
public class AdminHomeBannerController {

    private static final int MAX_BANNERS = 5;

    private final HomeBannerRepository homeBannerRepository;
    private final FileStorageService fileStorageService;

    @Value("${file.base-url}")
    private String fileBaseUrl;

    // ================= CREATE BANNER =================
    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Create a new homepage banner", description = "Upload image + details. Max 5 banners allowed.")
    public ResponseEntity<?> createBanner(
            @RequestPart(value = "image") MultipartFile image,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "subtitle", required = false) String subtitle,
            @RequestParam(value = "buttonText", required = false) String buttonText,
            @RequestParam(value = "linkUrl", required = false) String linkUrl,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "active", required = false, defaultValue = "true") Boolean active) {
        try {
            long currentCount = homeBannerRepository.count();
            if (currentCount >= MAX_BANNERS) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Maximum " + MAX_BANNERS + " banners allowed. Delete an existing one first.", null, false));
            }

            if (image == null || image.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Banner image is required", null, false));
            }

            String fileName = fileStorageService.storeFile(image);

            HomeBanner banner = new HomeBanner();
            banner.setId(UUID.randomUUID().toString());
            banner.setImageUrl(fileBaseUrl + fileName);
            banner.setTitle(title);
            banner.setSubtitle(subtitle);
            banner.setButtonText(buttonText);
            banner.setLinkUrl(linkUrl);
            banner.setDisplayOrder(displayOrder != null ? displayOrder : (int) (currentCount + 1));
            banner.setActive(active != null ? active : true);
            banner.setCreatedAt(LocalDateTime.now());
            banner.setUpdatedAt(LocalDateTime.now());

            HomeBanner saved = homeBannerRepository.save(banner);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Banner created successfully", saved, true));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to upload image: " + e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= UPDATE BANNER =================
    @PutMapping(value = "/{bannerId}", consumes = {"multipart/form-data"})
    @Operation(summary = "Update an existing banner", description = "Image is optional — only replaces if a new file is sent")
    public ResponseEntity<?> updateBanner(
            @PathVariable String bannerId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "subtitle", required = false) String subtitle,
            @RequestParam(value = "buttonText", required = false) String buttonText,
            @RequestParam(value = "linkUrl", required = false) String linkUrl,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "active", required = false) Boolean active) {
        try {
            Optional<HomeBanner> bannerOpt = homeBannerRepository.findById(bannerId);
            if (bannerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Banner not found", null, false));
            }

            HomeBanner banner = bannerOpt.get();

            if (image != null && !image.isEmpty()) {
                if (banner.getImageUrl() != null && !banner.getImageUrl().isEmpty()) {
                    String oldFileName = banner.getImageUrl().replace(fileBaseUrl, "");
                    try { fileStorageService.deleteFile(oldFileName); }
                    catch (Exception e) { System.err.println("Failed to delete old banner image: " + e.getMessage()); }
                }
                String fileName = fileStorageService.storeFile(image);
                banner.setImageUrl(fileBaseUrl + fileName);
            }

            if (title != null)        banner.setTitle(title);
            if (subtitle != null)     banner.setSubtitle(subtitle);
            if (buttonText != null)   banner.setButtonText(buttonText);
            if (linkUrl != null)      banner.setLinkUrl(linkUrl);
            if (displayOrder != null) banner.setDisplayOrder(displayOrder);
            if (active != null)       banner.setActive(active);

            banner.setUpdatedAt(LocalDateTime.now());

            HomeBanner updated = homeBannerRepository.save(banner);

            return ResponseEntity.ok(new ApiResponse<>("Banner updated successfully", updated, true));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to upload image: " + e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= DELETE BANNER =================
    @DeleteMapping("/{bannerId}")
    @Operation(summary = "Delete a banner")
    public ResponseEntity<?> deleteBanner(@PathVariable String bannerId) {
        try {
            Optional<HomeBanner> bannerOpt = homeBannerRepository.findById(bannerId);
            if (bannerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Banner not found", null, false));
            }

            HomeBanner banner = bannerOpt.get();
            if (banner.getImageUrl() != null && !banner.getImageUrl().isEmpty()) {
                try {
                    String fileName = banner.getImageUrl().replace(fileBaseUrl, "");
                    fileStorageService.deleteFile(fileName);
                } catch (Exception e) {
                    System.err.println("Failed to delete banner image: " + e.getMessage());
                }
            }

            homeBannerRepository.deleteById(bannerId);

            return ResponseEntity.ok(new ApiResponse<>("Banner deleted successfully", null, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= GET ALL BANNERS (Admin — includes inactive) =================
    @GetMapping
    @Operation(summary = "Get all banners for admin panel")
    public ResponseEntity<?> getAllBanners() {
        try {
            List<HomeBanner> banners = homeBannerRepository.findAllByOrderByDisplayOrderAsc();
            return ResponseEntity.ok(new ApiResponse<>("Success", banners, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= GET BANNER BY ID =================
    @GetMapping("/{bannerId}")
    @Operation(summary = "Get a single banner by ID")
    public ResponseEntity<?> getBanner(@PathVariable String bannerId) {
        try {
            return homeBannerRepository.findById(bannerId)
                    .map(b -> ResponseEntity.ok(new ApiResponse<>("Success", b, true)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>("Banner not found", null, false)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= TOGGLE STATUS =================
    @PatchMapping("/{bannerId}/status")
    @Operation(summary = "Toggle banner active status")
    public ResponseEntity<?> updateStatus(@PathVariable String bannerId, @RequestParam Boolean active) {
        try {
            Optional<HomeBanner> bannerOpt = homeBannerRepository.findById(bannerId);
            if (bannerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Banner not found", null, false));
            }

            HomeBanner banner = bannerOpt.get();
            banner.setActive(active);
            banner.setUpdatedAt(LocalDateTime.now());

            HomeBanner updated = homeBannerRepository.save(banner);

            return ResponseEntity.ok(new ApiResponse<>("Status updated successfully", updated, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // ================= DTO =================
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;

        public ApiResponse(String message, T data, boolean success) {
            this.message = message;
            this.data = data;
            this.success = success;
        }

        public String getMessage() { return message; }
        public T getData() { return data; }
        public boolean isSuccess() { return success; }
    }
}