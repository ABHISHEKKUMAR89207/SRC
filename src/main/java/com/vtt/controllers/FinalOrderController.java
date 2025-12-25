package com.vtt.controllers;

import com.vtt.dtoforSrc.KhataBookRequestDTO;
import com.vtt.entities.*;
import com.vtt.repository.*;
import com.vtt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    WorkerKhataBookController workerKhataBookController;
    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductSetsRepository productSetsRepository;

    @Autowired
    private FabricRepository fabricRepository;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private DiscountRepository discountRepository;
    @PostMapping("/recalculate-order-prices")
    public ResponseEntity<String> recalculateOrderPrices(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String orderId) {

        try {
            // Extract username from token
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            // Fetch user
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fetch order
            ProductOrder order = productOrderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Check if order is already approved or rejected
            if (order.getApproved().equalsIgnoreCase("approved")) {
                return ResponseEntity.badRequest().body("Error: Cannot recalculate prices for approved orders");
            }
            if (order.getApproved().equalsIgnoreCase("rejected")) {
                return ResponseEntity.badRequest().body("Error: Cannot recalculate prices for rejected orders");
            }

            // Get user discounts
            List<Discount> userDiscounts = discountRepository.findAll()
                    .stream()
                    .filter(d -> d.getUser() != null &&
                            d.getUser().getUserId().equals(order.getUser().getUserId()))
                    .toList();

            double recalculatedTotalAmount = 0.0;

            // Recalculate product entries
            List<ProductOrder.ProductEntry> recalculatedProductEntries = new ArrayList<>();

            for (ProductOrder.ProductEntry productEntry : order.getProductEntries()) {
                ProductInventory inventory = productEntry.getProductInventory();

                // Find discounted price for this product
                Double discountedPrice = null;
                if (!userDiscounts.isEmpty()) {
                    discountedPrice = findDiscountedPriceForProduct(inventory.getId(), userDiscounts);
                }

                // If no discount found, use wholesale price
                if (discountedPrice == null || discountedPrice == 0) {
                    Fabric fabric = inventory.getFabric();
                    discountedPrice = fabric.getWholesalePrice();
                }

                // Recalculate ordered sizes with new price
                List<ProductOrder.OrderedSizeQuantity> recalculatedSizes = new ArrayList<>();
                double productEntryTotal = 0.0;

                for (ProductOrder.OrderedSizeQuantity orderedSize : productEntry.getOrderedSizes()) {
                    double quantity = orderedSize.getQuantity();
                    double sizeTotal = discountedPrice * quantity;
                    productEntryTotal += sizeTotal;

                    // Create new size quantity with updated price
                    ProductOrder.OrderedSizeQuantity recalculatedSize =
                            new ProductOrder.OrderedSizeQuantity(
                                    orderedSize.getLabel(),
                                    orderedSize.getQuantity(),
                                    discountedPrice
                            );
                    recalculatedSizes.add(recalculatedSize);
                }

                recalculatedTotalAmount += productEntryTotal;

                // Create new product entry with recalculated sizes
                ProductOrder.ProductEntry recalculatedEntry =
                        new ProductOrder.ProductEntry(inventory, recalculatedSizes);
                recalculatedProductEntries.add(recalculatedEntry);
            }

            // Recalculate sets
            List<ProductOrder.OrderedSets> recalculatedSets = new ArrayList<>();

            for (ProductOrder.OrderedSets orderedSet : order.getSets()) {
                ProductSets productSet = orderedSet.getProductSet();

                // Find discounted price for this set
                Double discountedPrice = null;
                if (!userDiscounts.isEmpty()) {
                    discountedPrice = findDiscountedPriceForProduct(productSet.getId(), userDiscounts);
                }

                // If no discount found, use wholesale price
                if (discountedPrice == null || discountedPrice == 0) {
                    Fabric fabric = productSet.getFabric();
                    discountedPrice = fabric.getWholesalePrice();
                }

                // Calculate total quantity in the set
                int totalSetQuantity = 0;
                if (productSet.getSizes() != null) {
                    for (ProductSets.SizeQuantity size : productSet.getSizes()) {
                        totalSetQuantity += size.getQuantity();
                    }
                }

                // Calculate set total
                double setTotal = discountedPrice * totalSetQuantity * orderedSet.getQuantity();
                recalculatedTotalAmount += setTotal;

                // Create new ordered set with updated price
                ProductOrder.OrderedSets recalculatedOrderedSet =
                        new ProductOrder.OrderedSets(
                                productSet,
                                orderedSet.getQuantity(),
                                discountedPrice
                        );
                recalculatedSets.add(recalculatedOrderedSet);
            }

            // Update the order with recalculated values
            order.setProductEntries(recalculatedProductEntries);
            order.setSets(recalculatedSets);
            order.setTotalAmount(recalculatedTotalAmount);

            // Save the updated order
            productOrderRepository.save(order);

            return ResponseEntity.ok("Order prices recalculated successfully. New total: " + recalculatedTotalAmount);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/final-order")
    public ResponseEntity<String> placeOrder(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String cartId,
            @RequestParam(required = false) String admin,
            @RequestParam(required = false) String userid) {

        try {
            // Extract username from token
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);
            User user;
            List<Discount> userDiscounts = null;
            if (admin != null && admin.equalsIgnoreCase("admin")) {
                System.out.println("Admin entered");
                if (userid != null && userid.isEmpty())
                    return ResponseEntity.badRequest().body("Error: user id not found");
                user = userRepository.findByUserId(userid);
                userDiscounts = discountRepository.findAll()
                        .stream()
                        .filter(d -> d.getUser() != null && d.getUser().getUserId().equals(userid))
                        .toList();

            }else {
                user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

//            if (!cart.getUser().getUserId().equals(user.getUserId())) {
//                return ResponseEntity.badRequest().body("Error: Unauthorized access to this cart");
//            }

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
                Double discountedPrice = null;
                if ( userDiscounts != null && !userDiscounts.isEmpty()) {
                    discountedPrice = findDiscountedPriceForProduct(inventory.getId(), userDiscounts);
                    if (discountedPrice != null) {
                        System.out.println("Using discounted price " + discountedPrice + " for set: " + inventory.getId());
                    }
                }
                double itemTotal = 0.0;
                List<ProductOrder.OrderedSizeQuantity> orderedSizes = new ArrayList<>();
                for (Cart.CartProductItem.SizeQuantity size : item.getSelectedSizes()) {
                    double qty = size.getQuantity();
                    Fabric fabric = inventory.getFabric();
                    if(discountedPrice==null||discountedPrice==0){
                        discountedPrice=fabric.getWholesalePrice();
                    }
                    itemTotal += discountedPrice * qty;
                    orderedSizes.add(new ProductOrder.OrderedSizeQuantity(size.getLabel(), (int) qty,discountedPrice));
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

                Double discountedPrice = null;
                if ( userDiscounts != null && !userDiscounts.isEmpty()) {

//              ProductInventory inventory =  productInventoryRepository.findFirstByFabricAndDisplayNamesCatAndColor(
//                            productSet.getFabric(),
//                            productSet.getDisplayNamesCat(),
//                            productSet.getColor()
//                    );
//                    System.out.println("inventory found --------------- " + inventory );

                    discountedPrice = findDiscountedPriceForProduct(productSet.getId(), userDiscounts);
                    if (discountedPrice != null) {
                        System.out.println("Using discounted price " + discountedPrice + " for set: " );
                    }
                }


                Fabric fabric = productSet.getFabric();
                if(discountedPrice==null||discountedPrice==0){
                    discountedPrice=fabric.getWholesalePrice();
                }
               double setTotal = discountedPrice*qty*setItem.getQuantity();
                totalAmount += setTotal;
                orderedSets.add(new ProductOrder.OrderedSets(productSet, setItem.getQuantity(),discountedPrice));
            }

            // Create and save the order
            ProductOrder order = new ProductOrder();
            order.setUser(user);
            order.setTotalAmount(totalAmount);
            order.setProductEntries(productEntries);
            order.setSets(orderedSets);
            order.setPayment(false); // Initial payment false
            if (admin != null && admin.equalsIgnoreCase("admin"))
            order.setApproved("approved");
            else
                order.setApproved("Pending");
            order.setOrderDate(LocalDateTime.now());

             order= productOrderRepository.save(order);

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

            if (admin != null && admin.equalsIgnoreCase("admin")){
                // Prepare request DTO for transaction
                KhataBookRequestDTO dto = new KhataBookRequestDTO();
                dto.setUserId(order.getUser().getUserId());
                dto.setAmount(order.getTotalAmount());  // assuming ProductOrder has amount
                dto.setType("credit"); // or "debit" depending on logic
                dto.setNote("Order approved transaction"+order.getId());
                dto.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                // Call the transaction logic directly
                workerKhataBookController.addTransaction(dto);
            }
            return ResponseEntity.ok("Order placed successfully with ID: " + order.getId());

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    private Double findDiscountedPriceForProduct(String productId, List<Discount> userDiscounts) {
        // Iterate through all discount entries and return the first matching product discount
        for (Discount discount : userDiscounts) {
            if (discount.getDiscountWithProducts() != null) {
                for (Discount.DiscountWithProduct discountProduct : discount.getDiscountWithProducts()) {
                    if (productId.equals(discountProduct.getProductId())) {
                        return discountProduct.getPrice();
                    }
                }
            }
        }
        return null; // No discount found for this product
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
