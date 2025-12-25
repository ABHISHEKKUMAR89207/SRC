package com.vtt.controllers;

import com.vtt.entities.ProductOrder;
import com.vtt.entities.User;
import com.vtt.repository.ProductOrderRepository;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/order-delivery")
public class OrderDeliveryController {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    /* =====================================================
       1️⃣ ADMIN API – ASSIGN ORDER TO SALESMAN
       ===================================================== */
    @PostMapping("/assign-salesman")
    public ResponseEntity<String> assignOrderToSalesman(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String orderId,
            @RequestParam String salesmanUserId
    ) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String adminEmail = jwtHelper.getUsernameFromToken(token);

            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            // 🔐 Admin check
//            if (!"ADMIN".equalsIgnoreCase(admin.getMainRole().toString())) {
//                return ResponseEntity.status(403).body("Only admin can assign salesman");
//            }

            ProductOrder order = productOrderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            User salesman = userRepository.findByUserId(salesmanUserId);
            if (salesman == null) {
                return ResponseEntity.badRequest().body("Salesman not found");
            }

            // Assign fields
            order.setAssignedForSalesman(salesman);
            order.setAssignedToSalesmanDate(LocalDateTime.now());
            order.setCurrentBySalesmanStatus("ASSIGNED");
            order.setSalesmanName(salesman.getName());

            productOrderRepository.save(order);

            return ResponseEntity.ok("Order assigned to salesman successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /* =====================================================
       2️⃣ SALESMAN API – UPDATE DELIVERY STATUS
       ===================================================== */
    @PostMapping("/update-by-salesman")
    public ResponseEntity<String> updateOrderBySalesman(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String orderId,
            @RequestParam String status,               // ASSIGNED, PICKED, OUT_FOR_DELIVERY, DELIVERED, FAILED
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String problem
    ) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String salesmanEmail = jwtHelper.getUsernameFromToken(token);

            User salesman = userRepository.findByEmail(salesmanEmail)
                    .orElseThrow(() -> new RuntimeException("Salesman not found"));

            ProductOrder order = productOrderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // 🔐 Ensure same salesman
            if (order.getAssignedForSalesman() == null ||
                    !order.getAssignedForSalesman().getUserId().equals(salesman.getUserId())) {
                return ResponseEntity.status(403).body("This order is not assigned to you");
            }

            // 🚫 LOCK: If already delivered
            if ("DELIVERED".equalsIgnoreCase(order.getCurrentBySalesmanStatus())) {
                return ResponseEntity.badRequest()
                        .body("Order already delivered. Further changes are not allowed.");
            }

            // Update fields
            order.setCurrentBySalesmanStatus(status);
            order.setCurrentBySalesmanLocation(location);
            order.setProblemReportedBySalesman(problem);
            order.setSalesmanName(salesman.getName());

            if ("DELIVERED".equalsIgnoreCase(status)) {
                order.setDeliveredBySalesmanDate(LocalDateTime.now());
            }

            productOrderRepository.save(order);

            return ResponseEntity.ok("Order updated successfully by salesman");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/admin-update-delivery")
    public ResponseEntity<String> adminUpdateOrderDelivery(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String orderId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String problem,
            @RequestParam(required = false) String newSalesmanUserId
    ) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String adminEmail = jwtHelper.getUsernameFromToken(token);

            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

//            // 🔐 Admin check
//            if (!"ADMIN".equalsIgnoreCase(admin.getMainRole().toString())) {
//                return ResponseEntity.status(403).body("Only admin can update delivery details");
//            }

            ProductOrder order = productOrderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Update delivery fields
            if (status != null) {
                order.setCurrentBySalesmanStatus(status);

                if ("DELIVERED".equalsIgnoreCase(status)) {
                    order.setDeliveredBySalesmanDate(LocalDateTime.now());
                }
            }

            if (location != null) {
                order.setCurrentBySalesmanLocation(location);
            }

            if (problem != null) {
                order.setProblemReportedBySalesman(problem);
            }

            // 🔄 Reassign salesman
            if (newSalesmanUserId != null) {
                User newSalesman = userRepository.findByUserId(newSalesmanUserId);
                if (newSalesman == null) {
                    return ResponseEntity.badRequest().body("New salesman not found");
                }

                order.setAssignedForSalesman(newSalesman);
                order.setSalesmanName(newSalesman.getName());
                order.setAssignedToSalesmanDate(LocalDateTime.now());
            }

            productOrderRepository.save(order);

            return ResponseEntity.ok("Order delivery details updated by admin");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/orders-by-salesman")
    public ResponseEntity<?> getOrdersBySalesman(
            @RequestParam String salesmanUserId
    ) {
        try {
            User salesman = userRepository.findByUserId(salesmanUserId);
            if (salesman == null) {
                return ResponseEntity.badRequest().body("Salesman not found");
            }

            List<ProductOrder> orders =
                    productOrderRepository.findByAssignedForSalesman(salesman);

            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
