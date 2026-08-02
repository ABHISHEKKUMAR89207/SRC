package com.vtt.retail;

import com.vtt.retail.entities.HomeBanner;
import com.vtt.retail.repository.HomeBannerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/banners")
@Tag(name = "Retail Banner Controller", description = "Public API for homepage carousel banners")
public class RetailBannerController {

    private final HomeBannerRepository homeBannerRepository;

    @GetMapping
    @Operation(summary = "Get active homepage banners", description = "Returns only active banners, ordered for the carousel")
    public ResponseEntity<?> getActiveBanners() {
        try {
            List<HomeBanner> banners = homeBannerRepository.findByActiveTrueOrderByDisplayOrderAsc();
            return ResponseEntity.ok(new ApiResponse<>("Success", banners, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

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