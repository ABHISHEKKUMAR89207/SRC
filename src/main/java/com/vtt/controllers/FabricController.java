package com.vtt.controllers;


import com.vtt.commonfunc.TokenUtils;
import com.vtt.entities.Fabric;
import com.vtt.entities.FabricHistory;
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

@RestController
@RequestMapping("/api/fabrics")
public class FabricController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    // Add new fabric or update existing one
    @PostMapping
    public ResponseEntity<?> addOrUpdateFabric(
            @RequestBody Fabric fabric,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            fabric.setUpdatedAt(Instant.now());

            if (fabric.getId() == null) {
                fabric.setCreatedAt(Instant.now());
                fabric.setPaymentDone(0);
                fabric.setAllTimeTotaluantityinMeter(fabric.getQuantityinMeter());
                Fabric savedFabric = mongoTemplate.save(fabric);
                return ResponseEntity.ok(savedFabric);
            } else {
                Query query = new Query(Criteria.where("id").is(fabric.getId()));
                Update update = new Update()
                        .set("fabricName", fabric.getFabricName())
                        .set("millFactory", fabric.getMillFactory())
                        .set("displayName", fabric.getDisplayName())
                        .set("quantityinMeter", fabric.getQuantityinMeter())
                        .set("buyingPrice", fabric.getBuyingPrice())
                        .set("wholesalePrice", fabric.getWholesalePrice())
                        .set("retailPrice", fabric.getRetailPrice())
//                        .set("totalAmount", fabric.getTotalAmount())
                        .set("maximumPrice", fabric.getMaximumPrice())
                        .set("updatedAt", fabric.getUpdatedAt());
                System.out.println("fdreyhgfhfjh======"+fabric.getMaximumPrice());
                mongoTemplate.updateFirst(query, update, Fabric.class);
                return ResponseEntity.ok(fabric);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Add fabric transaction (invoice)
    @PostMapping("/{fabricId}/transactions")
    public ResponseEntity<?> addFabricTransaction(
            @PathVariable String fabricId,
            @RequestBody FabricHistory transaction,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            Fabric fabric = mongoTemplate.findById(fabricId, Fabric.class);
            if (fabric == null) {
                return ResponseEntity.notFound().build();
            }

            transaction.setFabric(fabric);
            transaction.setCreatedAt(Instant.now());

            FabricHistory savedTransaction = mongoTemplate.save(transaction);

            Update fabricUpdate = new Update()
                    .set("updatedAt", Instant.now());

            if (transaction.getQuantityinMeter() != 0) {
                double newQuantity = fabric.getQuantityinMeter() + transaction.getQuantityinMeter();
                fabricUpdate.set("quantityinMeter", newQuantity);
                fabricUpdate.set("allTimeTotaluantityinMeter", fabric.getAllTimeTotaluantityinMeter()+transaction.getQuantityinMeter());
            }

            if (transaction.getCredit() != null) {
                double newPaymentDone = fabric.getPaymentDone() + transaction.getCredit();
                fabricUpdate.set("paymentDone", newPaymentDone);
            }

            if (transaction.getDebit() != null) {
                double newTotalAmount = fabric.getTotalAmount() + transaction.getDebit();
                fabricUpdate.set("totalAmount", newTotalAmount);
            }

            Query query = new Query(Criteria.where("id").is(fabricId));
            mongoTemplate.updateFirst(query, fabricUpdate, Fabric.class);

            return ResponseEntity.ok(savedTransaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Get all fabrics
    @GetMapping
    public ResponseEntity<?> getAllFabrics(
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or VIEWER can access this endpoint");
            }

            List<Fabric> fabrics = mongoTemplate.findAll(Fabric.class);
            return ResponseEntity.ok(fabrics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Get fabric by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getFabricById(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or VIEWER can access this endpoint");
            }

            Fabric fabric = mongoTemplate.findById(id, Fabric.class);
            if (fabric == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(fabric);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Get fabric history by fabric ID
    @GetMapping("/{fabricId}/history")
    public ResponseEntity<?> getFabricHistory(
            @PathVariable String fabricId,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN &&
                    requestingUser.getMainRole() != MainRole.ADMIN) {  // fix duplicate check
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or VIEWER can access this endpoint");
            }

            Fabric fabric = mongoTemplate.findById(fabricId, Fabric.class);
            if (fabric == null) {
                return ResponseEntity.notFound().build();
            }

            Query query = new Query(Criteria.where("fabric").is(fabric));  // ✅ correct DBRef query
            List<FabricHistory> history = mongoTemplate.find(query, FabricHistory.class);

            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }


    // Delete fabric
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFabric(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            Query historyQuery = new Query(Criteria.where("fabric.$id").is(id));
            mongoTemplate.remove(historyQuery, FabricHistory.class);

            Query fabricQuery = new Query(Criteria.where("id").is(id));
            mongoTemplate.remove(fabricQuery, Fabric.class);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }


    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<?> deleteFabricTransaction(
            @PathVariable String transactionId,
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            FabricHistory transaction = mongoTemplate.findById(transactionId, FabricHistory.class);
            if (transaction == null) {
                return ResponseEntity.notFound().build();
            }

            // Optional: Update related Fabric data (quantity, payment, etc.)
            Fabric fabric = transaction.getFabric();
            if (fabric != null) {
                Update update = new Update();
                if (transaction.getQuantityinMeter() != 0) {
                    double newQuantity = fabric.getQuantityinMeter() - transaction.getQuantityinMeter();
                    update.set("quantityinMeter", newQuantity);
                }
                if (transaction.getCredit() != null) {
                    double newPaymentDone = fabric.getPaymentDone() - transaction.getCredit();
                    update.set("paymentDone", newPaymentDone);
                }
                if (transaction.getDebit() != null) {
                    double newTotalAmount = fabric.getTotalAmount() - transaction.getDebit();
                    update.set("totalAmount", newTotalAmount);
                }
                update.set("updatedAt", Instant.now());

                Query fabricQuery = new Query(Criteria.where("id").is(fabric.getId()));
                mongoTemplate.updateFirst(fabricQuery, update, Fabric.class);
            }

            // Delete the transaction
            Query query = new Query(Criteria.where("id").is(transactionId));
            mongoTemplate.remove(query, FabricHistory.class);

            return ResponseEntity.ok("Transaction deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

}