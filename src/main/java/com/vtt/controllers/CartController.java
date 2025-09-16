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
}
