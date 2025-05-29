package com.vtt.controller;

import com.vtt.dtoforSrc.ProductOrderDTO;
import com.vtt.entities.Fabric;
import com.vtt.entities.ProductInventory;
import com.vtt.entities.ProductInventory.SizeQuantity;
import com.vtt.entities.ProductOrder;
import com.vtt.entities.ProductOrder.OrderedSizeQuantity;
import com.vtt.entities.ProductOrder.ProductEntry;
import com.vtt.entities.User;
import com.vtt.repository.ProductInventoryRepository;
import com.vtt.repository.ProductOrderRepository;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/productOrders")
@RequiredArgsConstructor
public class ProductOrderController {

    private final ProductOrderRepository orderRepository;
    private final ProductInventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;

    // Create Order
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody ProductOrderDTO orderDTO) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ProductEntry> productEntries = new ArrayList<>();
        double totalAmount = 0.0;

        for (ProductOrderDTO.ProductEntryDTO entryDTO : orderDTO.getProductEntries()) {
            ProductInventory inventory = inventoryRepository.findById(entryDTO.getProductInventoryId())
                    .orElseThrow(() -> new RuntimeException("Product inventory not found: " + entryDTO.getProductInventoryId()));

            // Get the fabric reference to access retail price
            Fabric fabric = inventory.getFabric();
            if (fabric == null) {
                return ResponseEntity.badRequest().body("Fabric reference not found for product: " + entryDTO.getProductInventoryId());
            }
            double retailPrice = fabric.getRetailPrice();

            List<OrderedSizeQuantity> orderedSizes = new ArrayList<>();

            for (ProductOrderDTO.OrderedSizeQuantityDTO osqDTO : entryDTO.getOrderedSizes()) {
                boolean updated = false;

                for (SizeQuantity sq : inventory.getSizes()) {
                    if (sq.getLabel().equalsIgnoreCase(osqDTO.getLabel())) {
                        if (sq.getQuantity() < osqDTO.getQuantity()) {
                            return ResponseEntity.badRequest().body("Not enough stock for size: " + osqDTO.getLabel());
                        }
                        sq.setQuantity(sq.getQuantity() - osqDTO.getQuantity());
                        updated = true;

                        // Calculate amount for this size quantity
                        totalAmount += retailPrice * osqDTO.getQuantity();
                        break;
                    }
                }

                if (!updated) {
                    return ResponseEntity.badRequest().body("Size not found: " + osqDTO.getLabel());
                }

                orderedSizes.add(new OrderedSizeQuantity(osqDTO.getLabel(), osqDTO.getQuantity()));
            }

            inventoryRepository.save(inventory); // save after update
            productEntries.add(new ProductEntry(inventory, orderedSizes));
        }

        // Save the order with calculated total amount
        ProductOrder order = new ProductOrder();
        order.setUser(user);
        order.setProductEntries(productEntries);
        order.setPayment(false); // Assume payment is done
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        return ResponseEntity.ok(order);
    }

    // Get All Orders for Logged-in User
    @GetMapping
    public ResponseEntity<?> getUserOrders(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ProductOrder> orders = orderRepository.findByUser(user);
        return ResponseEntity.ok(orders);
    }

    // Delete Order by ID (revert inventory)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String orderId) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).body("Unauthorized to delete this order.");
        }

        // Revert inventory
        for (ProductEntry entry : order.getProductEntries()) {
            ProductInventory inventory = entry.getProductInventory();
            List<SizeQuantity> sizeQuantities = inventory.getSizes();

            for (OrderedSizeQuantity osq : entry.getOrderedSizes()) {
                for (SizeQuantity sq : sizeQuantities) {
                    if (sq.getLabel().equalsIgnoreCase(osq.getLabel())) {
                        sq.setQuantity(sq.getQuantity() + osq.getQuantity());
                        break;
                    }
                }
            }

            inventoryRepository.save(inventory);
        }

        orderRepository.delete(order);

        return ResponseEntity.ok("Order deleted and inventory reverted.");
    }
    // Get All Orders (Admin use)
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // You can add a role check here if needed


        List<ProductOrder> allOrders = orderRepository.findAll();
        return ResponseEntity.ok(allOrders);
    }


    @PatchMapping("/{orderId}/approve")
    public ResponseEntity<?> updateApprovalStatus(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String orderId,
            @RequestParam String approved) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Optional: Add role check for admin only
        // if (!user.getRole().equals("ADMIN")) return ResponseEntity.status(403).body("Unauthorized access");

        ProductOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setApproved(approved);
        orderRepository.save(order);

        return ResponseEntity.ok("Order approval status updated to: " + approved);
    }

}