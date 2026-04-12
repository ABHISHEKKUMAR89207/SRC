package com.vtt.controllers;

import com.vtt.dtos.CartRequestDTO;
import com.vtt.dtos.CartResponseDTO;
import com.vtt.entities.Cart;
import com.vtt.entities.ProductInventory;
import com.vtt.entities.ProductSets;
import com.vtt.entities.User;
import com.vtt.repository.CartRepository;
import com.vtt.repository.ProductInventoryRepository;
import com.vtt.repository.ProductSetsRepository;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductSetsRepository productSetsRepository;

    @Autowired
    private JwtHelper jwtHelper; // JWT helper to extract username

    @PostMapping("/save")
    public ResponseEntity<String> saveCart(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody CartRequestDTO cartRequestDTO) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

            Cart cart = cartRepository.findByUser(user).orElse(new Cart());
            cart.setUser(user);

            cart.setProducts(cartRequestDTO.getProducts().stream().map(productDTO -> {
                Cart.CartProductItem productItem = new Cart.CartProductItem();
                ProductInventory product = productInventoryRepository.findById(productDTO.getProductInventoryId())
                        .orElseThrow(() -> new RuntimeException("Product inventory not found"));
                productItem.setProductInventory(product);
                productItem.setSelectedSizes(productDTO.getSelectedSizes().stream().map(sizeDTO ->
                        new Cart.CartProductItem.SizeQuantity(sizeDTO.getLabel(), sizeDTO.getQuantity())
                ).collect(Collectors.toList()));
                return productItem;
            }).collect(Collectors.toList()));

            cart.setSets(cartRequestDTO.getSets().stream().map(setDTO -> {
                Cart.CartSetItem setItem = new Cart.CartSetItem();
                ProductSets productSet = productSetsRepository.findById(setDTO.getProductSetId())
                        .orElseThrow(() -> new RuntimeException("Product set not found"));
                setItem.setProductSet(productSet);
                setItem.setQuantity(setDTO.getQuantity());
                return setItem;
            }).collect(Collectors.toList()));

            Cart savedCart = cartRepository.save(cart);
            return ResponseEntity.ok("Cart saved successfully with id: " + savedCart.getId());

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/user")
    public ResponseEntity<?> getCartByUser(@RequestHeader("Authorization") String tokenHeader) {
        try {
            // Extract token and get username
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            // Find user
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

            // Find cart
            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Cart not found for this user"));

            // Build response DTO
            CartResponseDTO responseDTO = new CartResponseDTO();
            responseDTO.setCartId(cart.getId());
            responseDTO.setUserId(cart.getUser().getUserId());
            responseDTO.setProducts(cart.getProducts().stream().map(productItem ->
                    new CartResponseDTO.ProductItemResponse(
                            productItem.getProductInventory().getId(),
                            productItem.getSelectedSizes().stream()
                                    .map(size -> new CartResponseDTO.SizeQuantityResponse(
                                            size.getLabel(),
                                            size.getQuantity()
                                    ))
                                    .toList()
                    )
            ).toList());
            responseDTO.setSets(cart.getSets().stream().map(setItem ->
                    new CartResponseDTO.SetItemResponse(
                            setItem.getProductSet().getId(),
                            setItem.getQuantity()
                    )
            ).toList());

            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            // Return error as plain text
            return ResponseEntity
                    .badRequest()
                    .body("Error: " + e.getMessage());
        }
    }


    // Helper method remains the same
    private CartResponseDTO buildCartResponse(Cart cart) {
        CartResponseDTO responseDTO = new CartResponseDTO();
        responseDTO.setCartId(cart.getId());
        responseDTO.setUserId(cart.getUser().getUserId());
        responseDTO.setProducts(cart.getProducts().stream().map(productItem -> new CartResponseDTO.ProductItemResponse(
                productItem.getProductInventory().getId(),
                productItem.getSelectedSizes().stream()
                        .map(size -> new CartResponseDTO.SizeQuantityResponse(size.getLabel(), size.getQuantity()))
                        .collect(Collectors.toList())
        )).collect(Collectors.toList()));
        responseDTO.setSets(cart.getSets().stream().map(setItem -> new CartResponseDTO.SetItemResponse(
                setItem.getProductSet().getId(),
                setItem.getQuantity()
        )).collect(Collectors.toList()));
        return responseDTO;
    }


    // Add these methods to your existing CartController class

    // API to increase or decrease product quantity (removes automatically if quantity becomes zero)
    @PutMapping("/product/update/{productInventoryId}")
    public ResponseEntity<?> updateProductQuantity(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String productInventoryId,
            @RequestParam String sizeLabel,
            @RequestParam int change) { // change = +1 for increase, -1 for decrease

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            // Check availability in inventory
            ProductInventory inventory = productInventoryRepository.findById(productInventoryId)
                    .orElseThrow(() -> new RuntimeException("Product not found in inventory"));

            // Find the size in inventory
            ProductInventory.SizeQuantity inventorySize = inventory.getSizes().stream()
                    .filter(size -> size.getLabel().equals(sizeLabel))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Size " + sizeLabel + " not available for this product"));

            boolean productFound = false;
            boolean sizeFound = false;

            for (Cart.CartProductItem productItem : cart.getProducts()) {
                if (productItem.getProductInventory().getId().equals(productInventoryId)) {
                    productFound = true;

                    for (Cart.CartProductItem.SizeQuantity cartSize : productItem.getSelectedSizes()) {
                        if (cartSize.getLabel().equals(sizeLabel)) {
                            sizeFound = true;
                            int newQuantity = cartSize.getQuantity() + change;

                            // Validate against inventory
                            if (newQuantity > inventorySize.getQuantity()) {
                                return ResponseEntity.badRequest().body(
                                        "Error: Only " + inventorySize.getQuantity() + " items available in size " + sizeLabel
                                );
                            }

                            if (newQuantity <= 0) {
                                // Remove this size from cart
                                productItem.getSelectedSizes().remove(cartSize);
                            } else {
                                cartSize.setQuantity(newQuantity);
                            }
                            break;
                        }
                    }

                    // If size not found and we're increasing (adding new size)
                    if (!sizeFound && change > 0) {
                        if (1 > inventorySize.getQuantity()) {
                            return ResponseEntity.badRequest().body(
                                    "Error: No stock available for size " + sizeLabel
                            );
                        }
                        // Add new size to existing product
                        Cart.CartProductItem.SizeQuantity newCartSize =
                                new Cart.CartProductItem.SizeQuantity(sizeLabel, 1);
                        productItem.getSelectedSizes().add(newCartSize);
                        sizeFound = true;
                    }

                    // If no sizes left for this product, remove the entire product item
                    if (productItem.getSelectedSizes().isEmpty()) {
                        cart.getProducts().remove(productItem);
                    }
                    break;
                }
            }

            // If product not found and we're increasing (adding new product)
            if (!productFound && change > 0) {
                if (1 > inventorySize.getQuantity()) {
                    return ResponseEntity.badRequest().body(
                            "Error: No stock available for size " + sizeLabel
                    );
                }
                // Add new product to cart
                Cart.CartProductItem newProductItem = new Cart.CartProductItem();
                newProductItem.setProductInventory(inventory);
                Cart.CartProductItem.SizeQuantity newSize =
                        new Cart.CartProductItem.SizeQuantity(sizeLabel, 1);
                newProductItem.setSelectedSizes(List.of(newSize));
                cart.getProducts().add(newProductItem);
            } else if (!productFound && change < 0) {
                return ResponseEntity.badRequest().body("Product not found in cart");
            }

            cartRepository.save(cart);
            return ResponseEntity.ok(buildCartResponse(cart));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // API to remove product or set from cart
    @DeleteMapping("/remove/{itemType}/{itemId}")
    public ResponseEntity<?> removeFromCart(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String itemType, // "product" or "set"
            @PathVariable String itemId,
            @RequestParam(required = false) String sizeLabel) { // sizeLabel only needed for product

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            boolean removed = false;

            if (itemType.equalsIgnoreCase("product")) {
                // Remove product from cart
                if (sizeLabel != null && !sizeLabel.isEmpty()) {
                    // Remove specific size of a product
                    for (Cart.CartProductItem productItem : cart.getProducts()) {
                        if (productItem.getProductInventory().getId().equals(itemId)) {
                            removed = productItem.getSelectedSizes().removeIf(size ->
                                    size.getLabel().equals(sizeLabel));

                            // If no sizes left, remove the entire product
                            if (productItem.getSelectedSizes().isEmpty()) {
                                cart.getProducts().remove(productItem);
                            }
                            break;
                        }
                    }
                    if (!removed) {
                        return ResponseEntity.badRequest().body("Product size not found in cart");
                    }
                } else {
                    // Remove entire product (all sizes)
                    removed = cart.getProducts().removeIf(productItem ->
                            productItem.getProductInventory().getId().equals(itemId));

                    if (!removed) {
                        return ResponseEntity.badRequest().body("Product not found in cart");
                    }
                }
            }
            else if (itemType.equalsIgnoreCase("set")) {
                // Remove set from cart
                removed = cart.getSets().removeIf(setItem ->
                        setItem.getProductSet().getId().equals(itemId));

                if (!removed) {
                    return ResponseEntity.badRequest().body("Product set not found in cart");
                }
            }
            else {
                return ResponseEntity.badRequest().body("Invalid item type. Use 'product' or 'set'");
            }

            cartRepository.save(cart);

            // Return success message with updated cart
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item removed successfully");
            response.put("cart", buildCartResponse(cart));
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Also add this method for updating set quantity (increase/decrease)
    @PutMapping("/set/update/{productSetId}")
    public ResponseEntity<?> updateSetQuantity(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable String productSetId,
            @RequestParam int change) { // change = +1 for increase, -1 for decrease

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            // Check availability in inventory
            ProductSets productSet = productSetsRepository.findById(productSetId)
                    .orElseThrow(() -> new RuntimeException("Product set not found"));

            boolean setFound = false;

            for (Cart.CartSetItem setItem : cart.getSets()) {
                if (setItem.getProductSet().getId().equals(productSetId)) {
                    setFound = true;
                    int newQuantity = setItem.getQuantity() + change;

                    // Validate against inventory (total quantity of all sizes combined)
                    int totalAvailable = productSet.getSizes().stream()
                            .mapToInt(ProductSets.SizeQuantity::getQuantity)
                            .sum();

                    if (newQuantity > totalAvailable) {
                        return ResponseEntity.badRequest().body(
                                "Error: Only " + totalAvailable + " sets available"
                        );
                    }

                    if (newQuantity <= 0) {
                        // Remove set from cart
                        cart.getSets().remove(setItem);
                    } else {
                        setItem.setQuantity(newQuantity);
                    }
                    break;
                }
            }

            // If set not found and we're increasing (adding new set)
            if (!setFound && change > 0) {
                int totalAvailable = productSet.getSizes().stream()
                        .mapToInt(ProductSets.SizeQuantity::getQuantity)
                        .sum();

                if (1 > totalAvailable) {
                    return ResponseEntity.badRequest().body("Error: Set is out of stock");
                }
                // Add new set to cart
                Cart.CartSetItem newSetItem = new Cart.CartSetItem();
                newSetItem.setProductSet(productSet);
                newSetItem.setQuantity(1);
                cart.getSets().add(newSetItem);
            } else if (!setFound && change < 0) {
                return ResponseEntity.badRequest().body("Product set not found in cart");
            }

            cartRepository.save(cart);
            return ResponseEntity.ok(buildCartResponse(cart));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
