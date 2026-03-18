package com.vtt.retail;

import com.vtt.retail.entities.RetailOrder;
import com.vtt.entities.ProductInventory;
import com.vtt.retail.repository.RetailOrderRepository;
import com.vtt.retail.repository.RetailProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/analytics")
@Tag(name = "Retail Analytics Controller", description = "API for sales analytics and dashboard reports")
public class RetailAnalyticsController {

    private final RetailOrderRepository retailOrderRepository;
    private final RetailProductRepository retailProductRepository;

    /**
     * Get dashboard summary
     */
    @GetMapping("/dashboard/summary")
    public ResponseEntity<?> getDashboardSummary() {
        try {
            DashboardSummary summary = new DashboardSummary();

            // Total orders
            long totalOrders = retailOrderRepository.count();
            summary.setTotalOrders(totalOrders);

            // Total revenue
            double totalRevenue = 0;
            List<RetailOrder> allOrders = retailOrderRepository.findAll();
            for (RetailOrder order : allOrders) {
                if ("DELIVERED".equalsIgnoreCase(order.getOrderStatus()) || 
                    "SHIPPED".equalsIgnoreCase(order.getOrderStatus())) {
                    totalRevenue += order.getTotalAmount();
                }
            }
            summary.setTotalRevenue(totalRevenue);

            // Total products
            summary.setTotalProducts(retailProductRepository.count());

            // Pending orders
            long pendingOrders = allOrders.stream()
                    .filter(o -> "PLACED".equalsIgnoreCase(o.getOrderStatus()))
                    .count();
            summary.setPendingOrders(pendingOrders);

            // Delivered orders
            long deliveredOrders = allOrders.stream()
                    .filter(o -> "DELIVERED".equalsIgnoreCase(o.getOrderStatus()))
                    .count();
            summary.setDeliveredOrders(deliveredOrders);

            // Cancelled orders
            long cancelledOrders = allOrders.stream()
                    .filter(o -> "CANCELLED".equalsIgnoreCase(o.getOrderStatus()))
                    .count();
            summary.setCancelledOrders(cancelledOrders);

            // Average order value
            double averageOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;
            summary.setAverageOrderValue(averageOrderValue);

            return ResponseEntity.ok(new ApiResponse<>("Success", summary, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get sales by month
     */
    @GetMapping("/sales/monthly")
    public ResponseEntity<?> getMonthlySales() {
        try {
            Map<String, Double> monthlySales = new LinkedHashMap<>();

            List<RetailOrder> orders = retailOrderRepository.findAll();

            // Group by month
            Map<YearMonth, Double> salesByMonth = orders.stream()
                    .filter(o -> "DELIVERED".equalsIgnoreCase(o.getOrderStatus()) || 
                               "SHIPPED".equalsIgnoreCase(o.getOrderStatus()))
                    .collect(Collectors.groupingBy(
                            order -> YearMonth.from(order.getPlacedAt()),
                            Collectors.summingDouble(RetailOrder::getTotalAmount)
                    ));

            // Convert to string format
            for (Map.Entry<YearMonth, Double> entry : salesByMonth.entrySet()) {
                monthlySales.put(entry.getKey().toString(), entry.getValue());
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", monthlySales, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get top selling products
     */
    @GetMapping("/top-products")
    public ResponseEntity<?> getTopSellingProducts(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<ProductInventory>products = retailProductRepository.findAll();

            List<ProductAnalytics> topProducts = products.stream()
                    .sorted(Comparator.comparing(ProductInventory::getTotalSales).reversed())
                    .limit(limit)
                    .map(product -> {
                        ProductAnalytics pa = new ProductAnalytics();
                        pa.setProductId(product.getId());
                        pa.setProductName(product.getNameOfProduct());
                        pa.setCategory(product.getCategory());
                        pa.setTotalSales(product.getTotalSales());
                        pa.setRating(product.getRating());
                        if (product.getSizes() != null && !product.getSizes().isEmpty()) {
                            pa.setPrice(product.getSizes().get(0).getPrice());
                        }
                        return pa;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>("Success", topProducts, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get sales by category
     */
    @GetMapping("/sales/category")
    public ResponseEntity<?> getSalesByCategory() {
        try {
            List<ProductInventory>products = retailProductRepository.findAll();

            Map<String, CategorySalesAnalytics> categorySales = new LinkedHashMap<>();

            for (ProductInventory product : products) {
                String category = product.getCategory();
                if (category != null) {
                    CategorySalesAnalytics csa = categorySales.computeIfAbsent(category, 
                            k -> new CategorySalesAnalytics(category, 0, 0.0));
                    
                    csa.setProductCount(csa.getProductCount() + 1);
                    csa.setTotalSales(csa.getTotalSales() + product.getTotalSales());
                }
            }

            List<CategorySalesAnalytics> result = new ArrayList<>(categorySales.values());
            result.sort(Comparator.comparing(CategorySalesAnalytics::getTotalSales).reversed());

            return ResponseEntity.ok(new ApiResponse<>("Success", result, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get order status distribution
     */
    @GetMapping("/order-status-distribution")
    public ResponseEntity<?> getOrderStatusDistribution() {
        try {
            List<RetailOrder> orders = retailOrderRepository.findAll();

            OrderStatusDistribution distribution = new OrderStatusDistribution();

            distribution.setPlaced((int) orders.stream()
                    .filter(o -> "PLACED".equalsIgnoreCase(o.getOrderStatus()))
                    .count());

            distribution.setShipped((int) orders.stream()
                    .filter(o -> "SHIPPED".equalsIgnoreCase(o.getOrderStatus()))
                    .count());

            distribution.setDelivered((int) orders.stream()
                    .filter(o -> "DELIVERED".equalsIgnoreCase(o.getOrderStatus()))
                    .count());

            distribution.setCancelled((int) orders.stream()
                    .filter(o -> "CANCELLED".equalsIgnoreCase(o.getOrderStatus()))
                    .count());

            distribution.setReturned((int) orders.stream()
                    .filter(o -> "RETURNED".equalsIgnoreCase(o.getOrderStatus()))
                    .count());

            return ResponseEntity.ok(new ApiResponse<>("Success", distribution, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get payment method distribution
     */
    @GetMapping("/payment-distribution")
    public ResponseEntity<?> getPaymentDistribution() {
        try {
            List<RetailOrder> orders = retailOrderRepository.findAll();

            Map<String, Integer> distribution = new LinkedHashMap<>();

            Map<String, Long> paymentMethods = orders.stream()
                    .collect(Collectors.groupingBy(
                            RetailOrder::getPaymentMethod,
                            Collectors.counting()
                    ));

            for (Map.Entry<String, Long> entry : paymentMethods.entrySet()) {
                distribution.put(entry.getKey(), entry.getValue().intValue());
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", distribution, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get average order value by category
     */
    @GetMapping("/average-order-value/category")
    public ResponseEntity<?> getAverageOrderValueByCategory() {
        try {
            List<RetailOrder> orders = retailOrderRepository.findAll();

            Map<String, Double> aovByCategory = new LinkedHashMap<>();

            Map<String, List<RetailOrder>> ordersByCategory = orders.stream()
                    .collect(Collectors.groupingBy(order -> {
                        // Extract category from first item
                        if (order.getItems() != null && !order.getItems().isEmpty()) {
                            // This is simplified - would need to look up category from product
                            return "General";
                        }
                        return "Unknown";
                    }));

            for (Map.Entry<String, List<RetailOrder>> entry : ordersByCategory.entrySet()) {
                double aov = entry.getValue().stream()
                        .mapToDouble(RetailOrder::getTotalAmount)
                        .average()
                        .orElse(0.0);
                aovByCategory.put(entry.getKey(), aov);
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", aovByCategory, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get inventory status
     */
    @GetMapping("/inventory-status")
    public ResponseEntity<?> getInventoryStatus() {
        try {
            List<ProductInventory>products = retailProductRepository.findAll();

            InventoryStatus status = new InventoryStatus();

            int lowStockProducts = 0;
            int outOfStockProducts = 0;
            int totalItems = 0;

            for (ProductInventory product : products) {
                if (product.getSizes() != null) {
                    for (ProductInventory.SizeQuantity size : product.getSizes()) {
                        totalItems += size.getQuantity();
                        if (size.getQuantity() == 0) {
                            outOfStockProducts++;
                        } else if (size.getQuantity() < 10) {
                            lowStockProducts++;
                        }
                    }
                }
            }

            status.setTotalItems(totalItems);
            status.setLowStockCount(lowStockProducts);
            status.setOutOfStockCount(outOfStockProducts);

            return ResponseEntity.ok(new ApiResponse<>("Success", status, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get recent orders
     */
    @GetMapping("/recent-orders")
    public ResponseEntity<?> getRecentOrders(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<RetailOrder> recentOrders = retailOrderRepository.findAll()
                    .stream()
                    .sorted(Comparator.comparing(RetailOrder::getPlacedAt).reversed())
                    .limit(limit)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>("Success", recentOrders, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardSummary {
        private long totalOrders;
        private double totalRevenue;
        private long totalProducts;
        private long pendingOrders;
        private long deliveredOrders;
        private long cancelledOrders;
        private double averageOrderValue;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAnalytics {
        private String productId;
        private String productName;
        private String category;
        private Double totalSales;
        private Double rating;
        private Double price;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySalesAnalytics {
        private String categoryName;
        private int productCount;
        private Double totalSales;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderStatusDistribution {
        private int placed;
        private int shipped;
        private int delivered;
        private int cancelled;
        private int returned;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryStatus {
        private int totalItems;
        private int lowStockCount;
        private int outOfStockCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}
