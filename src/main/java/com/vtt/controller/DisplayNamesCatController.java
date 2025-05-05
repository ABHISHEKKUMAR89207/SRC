package com.vtt.controller;


import com.vtt.commonfunc.TokenUtils;
import com.vtt.entities.DisplayNamesCat;
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

import java.util.List;

@RestController
@RequestMapping("/api/display-names-categories")
public class DisplayNamesCatController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    // Create or update a display name category
    @PostMapping
    public ResponseEntity<?> addOrUpdateDisplayNamesCat(
            @RequestBody DisplayNamesCat displayNamesCat,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            if (displayNamesCat.getId() == null) {
                // Create new entry
                DisplayNamesCat savedEntry = mongoTemplate.save(displayNamesCat);
                return ResponseEntity.ok(savedEntry);
            } else {
                // Update existing entry
                Query query = new Query(Criteria.where("id").is(displayNamesCat.getId()));
                Update update = new Update()
                        .set("categoryName", displayNamesCat.getCategoryName())
                        .set("subCategoryName", displayNamesCat.getSubCategoryName())
                        .set("productName", displayNamesCat.getProductName())
                        .set("productDescription", displayNamesCat.getProductDescription())
                        .set("manufacturerName", displayNamesCat.getManufacturerName())
                        .set("websiteName", displayNamesCat.getWebsiteName())
                        .set("imageUrl", displayNamesCat.getImageUrl());

                mongoTemplate.updateFirst(query, update, DisplayNamesCat.class);
                return ResponseEntity.ok(displayNamesCat);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Get all display name categories
    @GetMapping
    public ResponseEntity<?> getAllDisplayNamesCats(
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or VIEWER can access this endpoint");
            }

            List<DisplayNamesCat> categories = mongoTemplate.findAll(DisplayNamesCat.class);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Get display name category by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDisplayNamesCatById(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or VIEWER can access this endpoint");
            }

            DisplayNamesCat category = mongoTemplate.findById(id, DisplayNamesCat.class);
            if (category == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Search display name categories by various fields
    @GetMapping("/search")
    public ResponseEntity<?> searchDisplayNamesCats(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String subCategoryName,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String manufacturerName,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or VIEWER can access this endpoint");
            }

            Query query = new Query();
            if (categoryName != null) {
                query.addCriteria(Criteria.where("categoryName").regex(categoryName, "i"));
            }
            if (subCategoryName != null) {
                query.addCriteria(Criteria.where("subCategoryName").regex(subCategoryName, "i"));
            }
            if (productName != null) {
                query.addCriteria(Criteria.where("productName").regex(productName, "i"));
            }
            if (manufacturerName != null) {
                query.addCriteria(Criteria.where("manufacturerName").regex(manufacturerName, "i"));
            }

            List<DisplayNamesCat> results = mongoTemplate.find(query, DisplayNamesCat.class);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Delete a display name category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDisplayNamesCat(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            Query query = new Query(Criteria.where("id").is(id));
            DisplayNamesCat category = mongoTemplate.findOne(query, DisplayNamesCat.class);

            if (category == null) {
                return ResponseEntity.notFound().build();
            }

            mongoTemplate.remove(query, DisplayNamesCat.class);
            return ResponseEntity.ok("Display name category deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }
}