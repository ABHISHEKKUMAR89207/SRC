package com.vtt.controllers;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.vtt.commonfunc.TokenUtils;
import com.vtt.entities.ApplySet;
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
@RequestMapping("/api/applysets")
public class ApplySetController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * Create ApplySet
     */
    @PostMapping
    public ResponseEntity<?> createApplySet(
            @RequestBody ApplySet applySet,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can create sets");
            }

            ApplySet savedSet = mongoTemplate.save(applySet);
            return ResponseEntity.ok(savedSet);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    /**
     * Update whole ApplySet (name, sizes list, etc.)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplySet(
            @PathVariable String id,
            @RequestBody ApplySet applySet,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can update sets");
            }

            Query query = new Query(Criteria.where("id").is(id));
            Update update = new Update()
                    .set("setName", applySet.getSetName())
                    .set("sizes", applySet.getSizes());

            UpdateResult result = mongoTemplate.updateFirst(query, update, ApplySet.class);

            if (result.getMatchedCount() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("ApplySet with id " + id + " not found");
            }

            return ResponseEntity.ok("ApplySet updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }


    /**
     * Update only a specific size quantity inside a set
     */
    @PatchMapping("/{id}/sizes/{label}")
    public ResponseEntity<?> updateSizeQuantity(
            @PathVariable String id,
            @PathVariable String label,
            @RequestParam int quantity,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can update size quantities");
            }

            // Find ApplySet
            ApplySet applySet = mongoTemplate.findById(id, ApplySet.class);
            if (applySet == null) {
                return ResponseEntity.notFound().build();
            }

            // Update the matching size
            boolean updated = false;
            for (ApplySet.SizeQuantity sq : applySet.getSizes()) {
                if (sq.getLabel().equalsIgnoreCase(label)) {
                    sq.setQuantity(quantity);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Size label not found in set");
            }

            mongoTemplate.save(applySet);
            return ResponseEntity.ok(applySet);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    /**
     * Get all ApplySets
     */
    @GetMapping
    public ResponseEntity<?> getAllApplySets(
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or VIEWER can access this endpoint");
            }

            List<ApplySet> sets = mongoTemplate.findAll(ApplySet.class);
            return ResponseEntity.ok(sets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    /**
     * Get ApplySet by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getApplySetById(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or VIEWER can access this endpoint");
            }

            ApplySet set = mongoTemplate.findById(id, ApplySet.class);
            if (set == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(set);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    /**
     * Delete ApplySet
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplySet(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can delete sets");
            }

            Query query = new Query(Criteria.where("id").is(id));
            DeleteResult result = mongoTemplate.remove(query, ApplySet.class);

            if (result.getDeletedCount() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("ApplySet with id " + id + " not found");
            }

            return ResponseEntity.ok("ApplySet deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

}
