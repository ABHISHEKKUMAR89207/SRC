package com.vtt.controller;


import com.vtt.dtoforSrc.OrderRequestDTO;
import com.vtt.entities.Fabric;
import com.vtt.entities.Order;
import com.vtt.entities.Order.SizeQuantity;
import com.vtt.commonfunc.TokenUtils;
import com.vtt.entities.User;
import com.vtt.otherclass.MainRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    // Create new order with DTO
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequestDTO orderDTO,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can create orders");
            }

            Order order = convertDtoToEntity(orderDTO);
            order.setCreatedAt(Instant.now());
            order.setUpdatedAt(Instant.now());

            Order savedOrder = mongoTemplate.save(order);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating order: " + e.getMessage());
        }
    }

    // Update existing order with DTO
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable String id,
            @RequestBody OrderRequestDTO orderDTO,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can update orders");
            }

            Order existingOrder = mongoTemplate.findById(id, Order.class);
            if (existingOrder == null) {
                return ResponseEntity.notFound().build();
            }

            updateEntityFromDto(existingOrder, orderDTO);
            existingOrder.setUpdatedAt(Instant.now());

            Order updatedOrder = mongoTemplate.save(existingOrder);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating order: " + e.getMessage());
        }
    }

    // Helper method to convert DTO to Entity
    private Order convertDtoToEntity(OrderRequestDTO orderDTO) {
        Order order = new Order();
        updateEntityFromDto(order, orderDTO);
        return order;
    }

    // Helper method to update entity from DTO
    private void updateEntityFromDto(Order order, OrderRequestDTO orderDTO) {
        order.setMasterNumber(orderDTO.getMasterNumber());
        order.setClientUserId(orderDTO.getClientUserId());
        order.setCategory(orderDTO.getCategory());
        order.setSubCategory(orderDTO.getSubCategory());
        order.setDisplayName(orderDTO.getDisplayName());
        order.setDisplayId(orderDTO.getDisplayId());
        order.setStatus(orderDTO.getStatus());

        // Process sizes
        if (orderDTO.getSizes() != null) {
            List<SizeQuantity> sizes = orderDTO.getSizes().stream()
                    .map(dto -> {
                        SizeQuantity sq = new SizeQuantity();
                        sq.setLabel(dto.getLabel());
                        sq.setQuantity(dto.getQuantity());
                        sq.setCompletedQuantity(dto.getCompletedQuantity() != null ?
                                dto.getCompletedQuantity() : 0);
                        return sq;
                    })
                    .collect(Collectors.toList());
            order.setSizes(sizes);
        }

        // Calculate total quantity
        if (orderDTO.getTotalQuantity() != null) {
            order.setTotalQuantity(orderDTO.getTotalQuantity());
        } else if (orderDTO.getSizes() != null) {
            int total = orderDTO.getSizes().stream()
                    .mapToInt(OrderRequestDTO.SizeQuantityDTO::getQuantity)
                    .sum();
            order.setTotalQuantity(total);
        } else {
            order.setTotalQuantity(0);
        }

        // Process fabric references
        if (orderDTO.getFabrics() != null) {
            List<Fabric> fabrics = orderDTO.getFabrics().stream()
                    .map(fabricId -> mongoTemplate.findById(fabricId, Fabric.class))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (fabrics.size() != orderDTO.getFabrics().size()) {
                throw new IllegalArgumentException("One or more fabric IDs are invalid");
            }
            order.setFabrics(fabrics);
        }
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (!hasAccess(requestingUser, MainRole.ADMIN, MainRole.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied");
            }

            List<Order> orders = mongoTemplate.findAll(Order.class);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (!hasAccess(requestingUser, MainRole.ADMIN, MainRole.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied");
            }

            Order order = mongoTemplate.findById(id, Order.class);
            if (order == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can delete orders");
            }

            Order order = mongoTemplate.findById(id, Order.class);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Order not found");
            }

            mongoTemplate.remove(order);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting order: " + e.getMessage());
        }
    }

    // Other methods remain the same as in your original controller...
    // (getOrdersByStatus, updateOrderStatus, updateSizeQuantities, deleteOrder, searchOrders)

    private boolean hasAccess(User user, MainRole... allowedRoles) {
        for (MainRole role : allowedRoles) {
            if (user.getMainRole() == role) {
                return true;
            }
        }
        return false;
    }
}