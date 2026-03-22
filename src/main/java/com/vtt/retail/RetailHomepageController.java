package com.vtt.retail;

import com.vtt.retail.entities.RetailCoupon;
import com.vtt.entities.ProductInventory;
import com.vtt.retail.repository.RetailCouponRepository;
import com.vtt.retail.repository.RetailProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/retail/homepage")
@Tag(name = "Retail Homepage Controller", description = "API for homepage content - banners, offers, featured products")
public class RetailHomepageController {

    private final RetailProductRepository retailProductRepository;
    private final RetailCouponRepository retailCouponRepository;
    private final RetailProductController retailProductController;

    /**
     * Get complete homepage data
     * Includes: Banners, Offers, Trending Products, Latest Products, Categories
     */
    @GetMapping("/data")
    public ResponseEntity<?> getHomepageData() {
        try {
            HomepageData homepageData = new HomepageData();

            // Get banners
            homepageData.setBanners(getBanners());

            // Get active offers/coupons
            homepageData.setOffers(getActiveOffers());

            // Get trending products
            homepageData.setTrendingProducts(getTrendingProducts());

            // Get latest products
            homepageData.setLatestProducts(getLatestProducts());

            // Get featured categories
            homepageData.setFeaturedCategories(getFeaturedCategories());

            return ResponseEntity.ok(new ApiResponse<>("Success", homepageData, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get all banners for homepage
     */
    @GetMapping("/banners")
    public ResponseEntity<?> getHomepageBanners() {
        try {
            List<Banner> banners = getBanners();
            return ResponseEntity.ok(new ApiResponse<>("Success", banners, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get all active offers
     */
    @GetMapping("/offers")
    public ResponseEntity<?> getHomepageOffers() {
        try {
            List<OfferDetail> offers = getActiveOffers();
            return ResponseEntity.ok(new ApiResponse<>("Success", offers, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get featured categories
     */
    @GetMapping("/categories")
    public ResponseEntity<?> getFeaturedCategoriesData() {
        try {
            List<FeaturedCategory> categories = getFeaturedCategories();
            return ResponseEntity.ok(new ApiResponse<>("Success", categories, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get trending products with short details
     */
    @GetMapping("/trending")
    public ResponseEntity<?> getHomepageTrending() {
        try {
            List<ProductSummary> products = getTrendingProducts();
            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get latest products with short details
     */
    @GetMapping("/latest")
    public ResponseEntity<?> getHomepageLatest() {
        try {
            List<ProductSummary> products = getLatestProducts();
            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get flash deals (products with coupons)
     */
    @GetMapping("/flash-deals")
    public ResponseEntity<?> getFlashDeals() {
        try {
            List<FlashDeal> flashDeals = new ArrayList<>();

            // Get active coupons with offers
            List<RetailCoupon> coupons = retailCouponRepository.findByIsActiveTrue();

            for (RetailCoupon coupon : coupons) {
                // Filter valid coupons
                if (coupon.getValidUntil().isAfter(LocalDateTime.now()) &&
                    coupon.getValidFrom().isBefore(LocalDateTime.now())) {

                    FlashDeal deal = new FlashDeal();
                    deal.setCouponCode(coupon.getCouponCode());
                    deal.setDescription(coupon.getDescription());
                    deal.setDiscountType(coupon.getDiscountType());

                    if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
                        deal.setDiscountValue(coupon.getDiscountPercentage());
                        deal.setDiscountLabel(coupon.getDiscountPercentage() + "% OFF");
                    } else {
                        deal.setDiscountValue(coupon.getFlatDiscount());
                        deal.setDiscountLabel("₹" + coupon.getFlatDiscount() + " OFF");
                    }

                    deal.setMinimumOrderValue(coupon.getMinimumOrderValue());
                    deal.setValidUntil(coupon.getValidUntil());
                    deal.setUsageRemaining(coupon.getMaximumUsageCount() - coupon.getCurrentUsageCount());

                    flashDeals.add(deal);
                }
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", flashDeals, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    /**
     * Get products on sale/discount
     */
    @GetMapping("/on-sale")
    public ResponseEntity<?> getOnSaleProducts() {
        try {
            List<ProductSummary> products = new ArrayList<>();

            // Get all trending products (considered as popular/sale items)
            List<ProductInventory>saleProducts = retailProductRepository.findTop10ByOrderByTotalSalesDesc();
            for (ProductInventory product : saleProducts) {
                ProductSummary summary = new ProductSummary();
                summary.setProductId(product.getId());
                summary.setProductName(product.getNameOfProduct());
                summary.setProductImage(product.getProductImage());
                summary.setCategory(product.getCategory());
                summary.setSubcategory(product.getSubcategory());
                summary.setRating(product.getRating());

                if (product.getSizes() != null && !product.getSizes().isEmpty()) {
                    summary.setPrice(product.getSizes().get(0).getPrice());
                    summary.setMinPrice(product.getSizes().stream()
                            .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                            .min()
                            .orElse(0.0));
                    summary.setMaxPrice(product.getSizes().stream()
                            .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                            .max()
                            .orElse(0.0));
                }

                products.add(summary);
            }

            return ResponseEntity.ok(new ApiResponse<>("Success", products, true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Error: " + e.getMessage(), null, false));
        }
    }

    // Helper methods

    private List<Banner> getBanners() {
        List<Banner> banners = new ArrayList<>();

        // Main banner
        Banner mainBanner = new Banner();
        mainBanner.setId("banner_main");
        mainBanner.setTitle("New Collection Arrival");
        mainBanner.setDescription("Explore the latest trends in fashion");
        mainBanner.setImageUrl("https://via.placeholder.com/1200x400?text=Main+Banner");
        mainBanner.setCtaText("Shop Now");
        mainBanner.setCtaLink("/shop/latest");
        mainBanner.setPosition(1);
        mainBanner.setActive(true);
        banners.add(mainBanner);

        // Secondary banner 1
        Banner banner2 = new Banner();
        banner2.setId("banner_secondary_1");
        banner2.setTitle("50% Off on Selected Items");
        banner2.setDescription("Limited time offer on premium collection");
        banner2.setImageUrl("https://via.placeholder.com/600x300?text=Banner+2");
        banner2.setCtaText("Grab Deal");
        banner2.setCtaLink("/shop/sale");
        banner2.setPosition(2);
        banner2.setActive(true);
        banners.add(banner2);

        // Secondary banner 2
        Banner banner3 = new Banner();
        banner3.setId("banner_secondary_2");
        banner3.setTitle("Free Shipping on Orders Above ₹500");
        banner3.setDescription("Fast delivery to your doorstep");
        banner3.setImageUrl("https://via.placeholder.com/600x300?text=Banner+3");
        banner3.setCtaText("Order Now");
        banner3.setCtaLink("/shop");
        banner3.setPosition(3);
        banner3.setActive(true);
        banners.add(banner3);

        return banners;
    }

    private List<OfferDetail> getActiveOffers() {
        List<OfferDetail> offers = new ArrayList<>();

        try {
            List<RetailCoupon> coupons = retailCouponRepository.findByIsActiveTrue();

            for (RetailCoupon coupon : coupons) {
                // Only include valid coupons
                if (coupon.getValidUntil().isAfter(LocalDateTime.now()) &&
                    coupon.getValidFrom().isBefore(LocalDateTime.now())) {

                    OfferDetail offer = new OfferDetail();
                    offer.setOfferId(coupon.getId());
                    offer.setOfferTitle(coupon.getCouponCode());
                    offer.setOfferDescription(coupon.getDescription());

                    if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
                        offer.setDiscountPercentage(coupon.getDiscountPercentage());
                        offer.setBadgeText(coupon.getDiscountPercentage() + "%");
                    } else {
                        offer.setFlatDiscount(coupon.getFlatDiscount());
                        offer.setBadgeText("₹" + coupon.getFlatDiscount());
                    }

                    offer.setMinimumOrderValue(coupon.getMinimumOrderValue());
                    offer.setCouponCode(coupon.getCouponCode());
                    offer.setValidUntil(coupon.getValidUntil());

                    long hoursRemaining = java.time.temporal.ChronoUnit.HOURS
                            .between(LocalDateTime.now(), coupon.getValidUntil());
                    offer.setHoursRemaining(hoursRemaining);

                    offers.add(offer);
                }
            }
        } catch (Exception e) {
            // Return empty offers if error
        }

        return offers;
    }

    private List<ProductSummary> getTrendingProducts() {
        List<ProductSummary> products = new ArrayList<>();

        try {
            List<ProductInventory>trendingList = retailProductRepository.findTop10ByOrderByTotalSalesDesc();

            for (ProductInventory product : trendingList) {
                ProductSummary summary = new ProductSummary();
                summary.setProductId(product.getId());
                summary.setProductName(product.getNameOfProduct());
                summary.setProductImage(product.getProductImage());
                summary.setCategory(product.getCategory());
                summary.setSubcategory(product.getSubcategory());
                summary.setRating(product.getRating());
                summary.setTotalSales(product.getTotalSales());

                if (product.getSizes() != null && !product.getSizes().isEmpty()) {
                    summary.setPrice(product.getSizes().get(0).getPrice());
                    summary.setMinPrice(product.getSizes().stream()
                            .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                            .min()
                            .orElse(0.0));
                    summary.setMaxPrice(product.getSizes().stream()
                            .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                            .max()
                            .orElse(0.0));
                }

                products.add(summary);
            }
        } catch (Exception e) {
            // Return empty list if error
        }

        return products;
    }

    private List<ProductSummary> getLatestProducts() {
        List<ProductSummary> products = new ArrayList<>();

        try {
            List<ProductInventory>latestList = retailProductRepository.findTop10ByOrderByLastedArrivedAtDesc();

            for (ProductInventory product : latestList) {
                ProductSummary summary = new ProductSummary();
                summary.setProductId(product.getId());
                summary.setProductName(product.getNameOfProduct());
                summary.setProductImage(product.getProductImage());
                summary.setCategory(product.getCategory());
                summary.setSubcategory(product.getSubcategory());
                summary.setRating(product.getRating());
                summary.setNew(true);

                if (product.getSizes() != null && !product.getSizes().isEmpty()) {
                    summary.setPrice(product.getSizes().get(0).getPrice());
                    summary.setMinPrice(product.getSizes().stream()
                            .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                            .min()
                            .orElse(0.0));
                    summary.setMaxPrice(product.getSizes().stream()
                            .mapToDouble(ProductInventory.SizeQuantity::getPrice)
                            .max()
                            .orElse(0.0));
                }

                products.add(summary);
            }
        } catch (Exception e) {
            // Return empty list if error
        }

        return products;
    }

    private List<FeaturedCategory> getFeaturedCategories() {
        List<FeaturedCategory> categories = new ArrayList<>();

        // Hardcoded featured categories - can be made dynamic from database
        String[] categoryNames = {"Men's Clothing", "Women's Clothing", "Kids Wear", "Accessories"};
        String[] categoryImages = {
            "https://via.placeholder.com/300x200?text=Mens",
            "https://via.placeholder.com/300x200?text=Womens",
            "https://via.placeholder.com/300x200?text=Kids",
            "https://via.placeholder.com/300x200?text=Accessories"
        };

        for (int i = 0; i < categoryNames.length; i++) {
            FeaturedCategory category = new FeaturedCategory();
            category.setCategoryId(i + 1);
            category.setCategoryName(categoryNames[i]);
            category.setCategoryImage(categoryImages[i]);
            category.setProductCount(getProductCountByCategory(categoryNames[i]));
            categories.add(category);
        }

        return categories;
    }

    private long getProductCountByCategory(String categoryName) {
        try {
            // Count products in category
            return retailProductRepository.findByCategory(categoryName, org.springframework.data.domain.Pageable.unpaged())
                    .getTotalElements();
        } catch (Exception e) {
            return 0;
        }
    }

    // DTOs
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomepageData {
        private List<Banner> banners;
        private List<OfferDetail> offers;
        private List<ProductSummary> trendingProducts;
        private List<ProductSummary> latestProducts;
        private List<FeaturedCategory> featuredCategories;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Banner {
        private String id;
        private String title;
        private String description;
        private String imageUrl;
        private String ctaText;
        private String ctaLink;
        private int position;
        private boolean active;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferDetail {
        private String offerId;
        private String offerTitle;
        private String offerDescription;
        private Double discountPercentage;
        private Double flatDiscount;
        private String badgeText;
        private Double minimumOrderValue;
        private String couponCode;
        private LocalDateTime validUntil;
        private long hoursRemaining;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSummary {
        private String productId;
        private String productName;
        private String productImage;
        private String category;
        private String subcategory;
        private Double price;
        private Double minPrice;
        private Double maxPrice;
        private Double rating;
        private Double totalSales;
        private boolean isNew;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeaturedCategory {
        private int categoryId;
        private String categoryName;
        private String categoryImage;
        private long productCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlashDeal {
        private String couponCode;
        private String description;
        private String discountType;
        private Double discountValue;
        private String discountLabel;
        private Double minimumOrderValue;
        private LocalDateTime validUntil;
        private long usageRemaining;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        private boolean success;
    }
}
