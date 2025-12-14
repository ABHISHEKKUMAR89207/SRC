package com.vtt.controllers;

import com.vtt.entities.Discount;
import com.vtt.entities.User;
import com.vtt.repository.DiscountRepository;
import com.vtt.repository.UserRepository;
import com.vtt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;


//    // ✅ Add or Update Discount (Admin only)
//    @PostMapping("/add")
//    public ResponseEntity<String> addDiscount(
//            @RequestHeader("Authorization") String tokenHeader,
//            @RequestParam String userId,
//            @RequestBody List<Discount.DiscountWithProduct> discountWithProducts) {
//
//        try {
//            // Extract username from JWT
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            User adminUser = userRepository.findByEmail(username)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
////            // Admin access only
////            if (!adminUser.getRole().equalsIgnoreCase("ADMIN")) {
////                return ResponseEntity.status(403).body("Forbidden: Only admin can add discounts");
////            }
//
//            // Linked user to assign discounts
//            User linkedUser = userRepository.findByUserId(userId);
//            if (linkedUser == null) {
//                return ResponseEntity.badRequest().body("Error: Linked user not found");
//            }
//
//            // Create or Update discount entry
//            Discount discount = new Discount();
//            discount.setUser(linkedUser);
//            discount.setDiscountWithProducts(discountWithProducts);
//
//            discountRepository.save(discount);
//
//            return ResponseEntity.ok("Discounts added/updated successfully for user: " + linkedUser.getUserName());
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }




// ✅ Add or Update Discount (Admin only)
@PostMapping("/add")
public ResponseEntity<String> addOrUpdateDiscount(
        @RequestHeader("Authorization") String tokenHeader,
        @RequestParam String userId,
        @RequestBody List<Discount.DiscountWithProduct> discountWithProducts) {

    try {
        // Extract username from JWT
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);

        User adminUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Optional: Restrict to Admin only
//        if (!adminUser.getRole().equalsIgnoreCase("ADMIN")) {
//            return ResponseEntity.status(403).body("Forbidden: Only admin can add or update discounts");
//        }

        // Find the linked user
        User linkedUser = userRepository.findByUserId(userId);
        if (linkedUser == null) {
            return ResponseEntity.badRequest().body("Error: Linked user not found");
        }

        // ✅ Check if discount already exists for this user
        Discount existingDiscount = discountRepository.findByUser(linkedUser);

        if (existingDiscount != null) {
            // Update existing record (replace the entire list)
            existingDiscount.setDiscountWithProducts(discountWithProducts);
            discountRepository.save(existingDiscount);
            return ResponseEntity.ok("Discounts updated successfully for user: " + linkedUser.getUserName());
        } else {
            // Create new record
            Discount newDiscount = new Discount();
            newDiscount.setUser(linkedUser);
            newDiscount.setDiscountWithProducts(discountWithProducts);
            discountRepository.save(newDiscount);
            return ResponseEntity.ok("Discounts added successfully for user: " + linkedUser.getUserName());
        }

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}



    // ✅ Get all discounts (Admin only)
    @GetMapping("/all")
    public ResponseEntity<?> getAllDiscounts(@RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User adminUser = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

//            if (!adminUser.getRole().equalsIgnoreCase("ADMIN")) {
//                return ResponseEntity.status(403).body("Forbidden: Only admin can view discounts");
//            }

            List<Discount> discounts = discountRepository.findAll();
            return ResponseEntity.ok(discounts);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    // ✅ Get discounts by user ID (Admin only)
    @GetMapping("/by-user")
    public ResponseEntity<?> getDiscountByUserId(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam String userId) {

        try {
            String token = tokenHeader.replace("Bearer ", "");
            String username = jwtHelper.getUsernameFromToken(token);

            User adminUser = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

//            if (!adminUser.getRole().equalsIgnoreCase("ADMIN")) {
//                return ResponseEntity.status(403).body("Forbidden: Only admin can view user discounts");
//            }

            List<Discount> discounts = discountRepository.findAll()
                    .stream()
                    .filter(d -> d.getUser() != null && d.getUser().getUserId().equals(userId))
                    .toList();

            return ResponseEntity.ok(discounts);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
