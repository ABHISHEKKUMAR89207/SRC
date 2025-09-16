package com.vtt.controllers;

import com.vtt.entities.*;
import com.vtt.repository.*;
import com.vtt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/finalorder")
public class FinalOrderController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductSetsRepository productSetsRepository;

    @Autowired
    private FabricRepository fabricRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/final-order")
    public ResponseEntity<String> placeOrder(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String cartId,
            @RequestParam(required = false) String admin) {

        try {
            // Extract username from token
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            if (admin != null && admin.equalsIgnoreCase("admin")) {
                System.out.println("Admin entered");
            }
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            if (!cart.getUser().getUserId().equals(user.getUserId())) {
                return ResponseEntity.badRequest().body("Error: Unauthorized access to this cart");
            }

            // Validate product inventory quantities
            // Validate product inventory quantities
            for (Cart.CartProductItem item : cart.getProducts()) {
                ProductInventory inventory = item.getProductInventory();

                for (Cart.CartProductItem.SizeQuantity selectedSize : item.getSelectedSizes()) {
                    boolean sizeFound = false;
                    for (ProductInventory.SizeQuantity availableSize : inventory.getSizes()) {
                        if (availableSize.getLabel().equals(selectedSize.getLabel())) {
                            sizeFound = true;
                            if (availableSize.getQuantity() < selectedSize.getQuantity()) {
                                return ResponseEntity.badRequest().body("Error: Not enough quantity for size "
                                        + selectedSize.getLabel() + " in product ID: " + inventory.getId());
                            }
                            break;
                        }
                    }
                    if (!sizeFound) {
                        return ResponseEntity.badRequest().body("Error: Size " + selectedSize.getLabel()
                                + " not available in product ID: " + inventory.getId());
                    }
                }
            }
            // Validate set quantities using totalQuantity
            for (Cart.CartSetItem setItem : cart.getSets()) {
                ProductSets productSet = setItem.getProductSet();



                if (setItem.getQuantity() > productSet.getTotalQuantity()) {
                    return ResponseEntity.badRequest()
                            .body("Error: Not enough quantity in set " + productSet.getSetName() + " (ID: " + productSet.getId() + ")");
                }
            }

            // Calculate total amount
            double totalAmount = 0.0;
            List<ProductOrder.ProductEntry> productEntries = new ArrayList<>();

            for (Cart.CartProductItem item : cart.getProducts()) {
                ProductInventory inventory = item.getProductInventory();
                double itemTotal = 0.0;
                List<ProductOrder.OrderedSizeQuantity> orderedSizes = new ArrayList<>();
                for (Cart.CartProductItem.SizeQuantity size : item.getSelectedSizes()) {
                    double qty = size.getQuantity();
                    Fabric fabric = inventory.getFabric();
                    itemTotal += fabric.getWholesalePrice() * qty;
                    orderedSizes.add(new ProductOrder.OrderedSizeQuantity(size.getLabel(), (int) qty));
                }
                totalAmount += itemTotal;
                productEntries.add(new ProductOrder.ProductEntry(inventory, orderedSizes));
            }

            List<ProductOrder.OrderedSets> orderedSets = new ArrayList<>();
            for (Cart.CartSetItem setItem : cart.getSets()) {
                ProductSets productSet = setItem.getProductSet();
                int qty=0;
                for (ProductSets.SizeQuantity size : productSet.getSizes()) {
                     qty += size.getQuantity() ;
                }
                Fabric fabric = productSet.getFabric();
               double setTotal = fabric.getWholesalePrice()*qty*setItem.getQuantity();
                totalAmount += setTotal;
                orderedSets.add(new ProductOrder.OrderedSets(productSet, setItem.getQuantity()));
            }

            // Create and save the order
            ProductOrder order = new ProductOrder();
            order.setUser(user);
            order.setTotalAmount(totalAmount);
            order.setProductEntries(productEntries);
            order.setSets(orderedSets);
            order.setPayment(false); // Initial payment false
            order.setApproved("Pending");
            order.setOrderDate(LocalDateTime.now());

            productOrderRepository.save(order);

// After saving the order, update inventory quantities
// Update product inventory sizes
            for (Cart.CartProductItem item : cart.getProducts()) {
                ProductInventory inventory = item.getProductInventory();

                List<ProductInventory.SizeQuantity> updatedSizes = new ArrayList<>();
                for (ProductInventory.SizeQuantity size : inventory.getSizes()) {
                    int orderedQty = 0;
                    // Find the ordered quantity for this size
                    for (Cart.CartProductItem.SizeQuantity orderedSize : item.getSelectedSizes()) {
                        if (orderedSize.getLabel().equals(size.getLabel())) {
                            orderedQty = (int) orderedSize.getQuantity();
                            break;
                        }
                    }
                    // Deduct the quantity
                    size.setQuantity(size.getQuantity() - orderedQty);
                    updatedSizes.add(size);
                }
                inventory.setSizes(updatedSizes);
                productInventoryRepository.save(inventory);
            }

// Update product sets quantities
            for (Cart.CartSetItem setItem : cart.getSets()) {
                ProductSets productSet = setItem.getProductSet();

                // Deduct the ordered quantity from totalQuantity
                int remainingQty = productSet.getTotalQuantity() - setItem.getQuantity();
                productSet.setTotalQuantity(remainingQty);

                productSetsRepository.save(productSet);
            }
            cartRepository.delete(cart);


            return ResponseEntity.ok("Order placed successfully with ID: " + order.getId());

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/disapprove-order")
    public ResponseEntity<String> disapproveOrder(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String orderId) {

        try {
            // Extract username from token
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ProductOrder order = productOrderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
if(order.getApproved().equalsIgnoreCase("Rejected")){
    return ResponseEntity.badRequest().body("Already rejected");
}
            // Optionally, you can restrict only admins to perform this action
            // Example: if (!user.getRole().equals("ADMIN")) { return ResponseEntity.status(403).body("Forbidden"); }

            // Reverse product inventory quantities
            for (ProductOrder.ProductEntry entry : order.getProductEntries()) {
                ProductInventory inventory = entry.getProductInventory();
                List<ProductInventory.SizeQuantity> updatedSizes = new ArrayList<>();

                for (ProductInventory.SizeQuantity size : inventory.getSizes()) {
                    int additionalQty = 0;
                    for (ProductOrder.OrderedSizeQuantity orderedSize : entry.getOrderedSizes()) {
                        if (size.getLabel().equals(orderedSize.getLabel())) {
                            additionalQty = orderedSize.getQuantity();
                            break;
                        }
                    }
                    size.setQuantity(size.getQuantity() + additionalQty);
                    updatedSizes.add(size);
                }
                inventory.setSizes(updatedSizes);
                productInventoryRepository.save(inventory);
            }

            // Reverse product sets quantities
            for (ProductOrder.OrderedSets orderedSet : order.getSets()) {
                ProductSets productSet = orderedSet.getProductSet();
                int restoredQty = productSet.getTotalQuantity() + orderedSet.getQuantity();
                productSet.setTotalQuantity(restoredQty);
                productSetsRepository.save(productSet);
            }

            // Update order status to rejected
            order.setApproved("Rejected");
            productOrderRepository.save(order);

            return ResponseEntity.ok("Order disapproved and inventory restored successfully.");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
