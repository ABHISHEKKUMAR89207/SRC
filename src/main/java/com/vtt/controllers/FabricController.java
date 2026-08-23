package com.vtt.controllers;

import com.vtt.FileStorage.FileStorageService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/fabrics")
public class FabricController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private FileStorageService fileStorageService;

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
                        .set("maximumPrice", fabric.getMaximumPrice())
                        .set("updatedAt", fabric.getUpdatedAt());
                mongoTemplate.updateFirst(query, update, Fabric.class);
                return ResponseEntity.ok(fabric);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Add fabric transaction (invoice) — now supports an optional paymentProof file (multipart/form-data)
    @PostMapping(value = "/{fabricId}/transactions", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addFabricTransaction(
            @PathVariable String fabricId,
            @RequestParam("invoiceNo") String invoiceNo,
            @RequestParam(value = "invoiceDate", required = false) Long invoiceDateMillis,
            @RequestParam(value = "quantityinMeter", defaultValue = "0") double quantityinMeter,
            @RequestParam(value = "credit", required = false) Double credit,
            @RequestParam(value = "debit", required = false) Double debit,
            @RequestParam(value = "paymentProof", required = false) MultipartFile paymentProofFile,
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

            FabricHistory transaction = new FabricHistory();
            transaction.setFabric(fabric);
            transaction.setInvoiceNo(invoiceNo);
            if (invoiceDateMillis != null) {
                transaction.setInvoiceDate(new Date(invoiceDateMillis));
            }
            transaction.setQuantityinMeter(quantityinMeter);
            transaction.setCredit(credit);
            transaction.setDebit(debit);
            transaction.setCreatedAt(Instant.now());

            // Store the payment proof image, if provided
            if (paymentProofFile != null && !paymentProofFile.isEmpty()) {
                String storedFileName = fileStorageService.storeFile(paymentProofFile);
                transaction.setPaymentProof(storedFileName);
            }

            FabricHistory savedTransaction = mongoTemplate.save(transaction);

            Update fabricUpdate = new Update()
                    .set("updatedAt", Instant.now());

            if (quantityinMeter != 0) {
                double newQuantity = fabric.getQuantityinMeter() + quantityinMeter;
                fabricUpdate.set("quantityinMeter", newQuantity);
                fabricUpdate.set("allTimeTotaluantityinMeter", fabric.getAllTimeTotaluantityinMeter() + quantityinMeter);
            }

            if (credit != null) {
                double newPaymentDone = fabric.getPaymentDone() + credit;
                fabricUpdate.set("paymentDone", newPaymentDone);
            }

            if (debit != null) {
                double newTotalAmount = fabric.getTotalAmount() + debit;
                fabricUpdate.set("totalAmount", newTotalAmount);
            }

            Query query = new Query(Criteria.where("id").is(fabricId));
            mongoTemplate.updateFirst(query, fabricUpdate, Fabric.class);

            return ResponseEntity.ok(savedTransaction);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to store payment proof: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }

    // Serve a stored payment proof image by its stored file name
    // NOTE: kept unauthenticated so Image.network(...) can load it directly without custom headers.
    // If these documents are sensitive, add a token/signature check here before serving.
    @GetMapping("/payment-proof/{fileName}")
    public ResponseEntity<byte[]> getPaymentProof(@PathVariable String fileName) {
        try {
            byte[] fileData = fileStorageService.loadFile(fileName);
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            String lower = fileName.toLowerCase();
            if (lower.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (lower.endsWith(".webp")) {
                mediaType = MediaType.valueOf("image/webp");
            }
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                    .body(fileData);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all fabrics
    @GetMapping
    public ResponseEntity<?> getAllFabrics(
            @RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
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
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
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
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            Fabric fabric = mongoTemplate.findById(fabricId, Fabric.class);
            if (fabric == null) {
                return ResponseEntity.notFound().build();
            }

            Query query = new Query(Criteria.where("fabric").is(fabric));
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

            // Optionally clean up the stored proof file
            if (transaction.getPaymentProof() != null && !transaction.getPaymentProof().isEmpty()) {
                try {
                    fileStorageService.deleteFile(transaction.getPaymentProof());
                } catch (IOException ignored) {
                    // non-fatal: proceed with deleting the transaction record regardless
                }
            }

            Query query = new Query(Criteria.where("id").is(transactionId));
            mongoTemplate.remove(query, FabricHistory.class);

            return ResponseEntity.ok("Transaction deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }
    }
}