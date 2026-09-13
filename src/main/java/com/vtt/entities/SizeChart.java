package com.vtt.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * SizeChart
 * ─────────
 * Linked to a ProductInventory via productId (String, matches ProductInventory._id).
 * The chart's title is NOT stored here — the frontend shows it using the
 * product's own name (fetched from ProductInventory), so there's nothing to
 * type or keep in sync.
 *
 * Columns are fixed (see SizeChartController.FIXED_COLUMNS): SIZE, WAIST,
 * LENGTH, HIP, THIGH, KNEE, BOTTOM, WEIGHT, DIMENSIONS — matching the
 * reference chart. Each entry in "rows" is one size's measurements as
 * column -> value.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "sizecharts")
public class SizeChart {

    @Id
    private String id;

    // Links this chart to ProductInventory.id
    private String productId;

    // One map per size row: fixed column name (SIZE, WAIST, LENGTH, HIP, THIGH,
    // KNEE, BOTTOM, WEIGHT, DIMENSIONS) -> value for that column
    private List<Map<String, String>> rows;

    // Optional footnote, e.g. "All the measurements calculated on inches."
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}