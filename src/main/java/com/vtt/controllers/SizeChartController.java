package com.vtt.controllers;

import com.vtt.entities.SizeChart;
import com.vtt.repository.SizeChartRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/sizecharts")
@Tag(name = "Size Chart Controller", description = "API for product size chart operations")
public class SizeChartController {

    private final SizeChartRepository sizeChartRepository;

    // Fixed set of columns, matching the reference size chart image.
    // Every chart uses exactly these columns — nothing is user-editable here.
    public static final List<String> FIXED_COLUMNS = List.of(
            "SIZE", "WAIST", "LENGTH", "HIP", "THIGH", "KNEE", "BOTTOM", "WEIGHT", "DIMENSIONS"
    );

    /**
     * 📐 Get the fixed column set the frontend should render as table headers.
     */
    @GetMapping("/columns")
    public ResponseEntity<?> getFixedColumns() {
        return ResponseEntity.ok(new ApiResponse<>("Success", FIXED_COLUMNS, true));
    }

    /**
     * ➕ Add or Update a size chart for a product.
     * This is an UPSERT: if a chart already exists for the given productId it is
     * updated in place, otherwise a new chart document is created. The same
     * endpoint powers both "Add Chart" and "Edit Chart" from the admin page.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addOrUpdateChart(@RequestBody SizeChart incoming) {
        try {
            if (incoming.getProductId() == null || incoming.getProductId().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("productId is required", null, false));
            }

            Optional<SizeChart> existingOpt = sizeChartRepository.findByProductId(incoming.getProductId());

            SizeChart toSave;
            if (existingOpt.isPresent()) {
                SizeChart existing = existingOpt.get();
                existing.setRows(incoming.getRows());
                existing.setNote(incoming.getNote());
                existing.setUpdatedAt(LocalDateTime.now());
                toSave = existing;
            } else {
                incoming.setId(null);
                incoming.setCreatedAt(LocalDateTime.now());
                incoming.setUpdatedAt(LocalDateTime.now());
                toSave = incoming;
            }

            SizeChart saved = sizeChartRepository.save(toSave);
            return ResponseEntity.ok(new ApiResponse<>("Success", saved, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * 📥 Get the size chart for a product by productId.
     * Returns success=true with data=null when no chart exists yet, so the
     * frontend can distinguish "no chart" from a real error.
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getChartByProductId(@PathVariable String productId) {
        try {
            Optional<SizeChart> chart = sizeChartRepository.findByProductId(productId);
            if (chart.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("No size chart found for this product", null, true));
            }
            return ResponseEntity.ok(new ApiResponse<>("Success", chart.get(), true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * 🗑 Delete the size chart for a product (used by "Remove Chart" in the admin edit page).
     */
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> deleteChartByProductId(@PathVariable String productId) {
        try {
            sizeChartRepository.deleteByProductId(productId);
            return ResponseEntity.ok(new ApiResponse<>("Deleted", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    @lombok.Getter
    @lombok.Setter
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}