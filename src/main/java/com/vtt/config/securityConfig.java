// Security Configuration file for each API

package com.vtt.config;

import com.vtt.security.JwtAuthenticationEntryPoint;
import com.vtt.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class securityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
              //  .cors(AbstractHttpConfigurer::disable)
                .cors(withDefaults -> {})
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/home/**")
                                .authenticated()
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/auth/otpforlogin").permitAll()
                                .requestMatchers("/auth/loginadmin").permitAll()
                                .requestMatchers("/api/user").permitAll()
                                .requestMatchers("/auth/refresh-token").permitAll()
                                .requestMatchers("/auth/health-trends/**").authenticated()
                                .requestMatchers("/api/products/**").permitAll()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/salary/**").permitAll()
                                .requestMatchers("/api/cart/**").permitAll()
                                .requestMatchers("/v3/api-docs").permitAll()
                                .requestMatchers("/api/backup").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/api/sleep-logs").permitAll()
                                .requestMatchers("/v3/api-docs/creatSleepLog").permitAll()
                                .requestMatchers("/logs").permitAll()
                                .requestMatchers("/ingredient/uploadImages").permitAll()
                                .requestMatchers("/api").permitAll()
                                .requestMatchers("/api/src-roles/**").permitAll()
                                .requestMatchers("/category-pricing/**").permitAll()
                                .requestMatchers("/api/admin/users/**").permitAll()
                                .requestMatchers("/api/khata/**").permitAll()
                                .requestMatchers("/api/user-work/**").permitAll()
                                .requestMatchers("/api/common/**").permitAll()
                                .requestMatchers("/api/serial-products/**").permitAll()
                                .requestMatchers("/api/inventory/**").permitAll()

                                .requestMatchers("/api/productOrders/**").permitAll()
                                .requestMatchers("/v3/api-docs").permitAll()
                                .requestMatchers("/api/sleep-logs").permitAll()
                                .requestMatchers("/v3/api-docs/creatSleepLog").permitAll()
                                .requestMatchers("/api/enquiry/**").permitAll()
                                .requestMatchers("/api/user/**").permitAll()
                                .requestMatchers("/api/combos/**").permitAll()
                                .requestMatchers("/api/cart/**").permitAll()
                                .requestMatchers("/api//category/**").permitAll()
                                .requestMatchers("/offers/**").permitAll()
                                .requestMatchers("/api/payment/**").permitAll()
                                .requestMatchers("/api/discount/**").permitAll()
                                .requestMatchers("/api/order-deliver/**").permitAll()
                                .requestMatchers("/api/orders/**").permitAll()
                                .requestMatchers("/api/retail/payment/**").permitAll()
                                .requestMatchers("/api/productinventory/**").permitAll()
                                .requestMatchers("/api/retail/delivery/**").permitAll()
                                .requestMatchers("/api/admin/retail/products/**").permitAll()
                                // ==================== RETAIL PRODUCT ENDPOINTS ====================
                                .requestMatchers(HttpMethod.GET, "/api/retail/products").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/category/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/subcategory/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/trending/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/latest/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/search/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/sizes/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/colors/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/products/price-range/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/retail/products/filter").permitAll()

                                // ==================== RETAIL USER PROFILE ENDPOINTS ====================
                                .requestMatchers(HttpMethod.POST, "/api/retail/users/profile").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/users/profile/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/retail/users/profile/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/users/exists/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/retail/users/profile/**").authenticated()

                                // ==================== RETAIL CART ENDPOINTS ====================
                                .requestMatchers(HttpMethod.POST, "/api/retail/cart/add").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/retail/cart/update").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/retail/cart/remove").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/retail/cart/clear/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/cart/**").authenticated()

                                // ==================== RETAIL WISHLIST ENDPOINTS ====================
                                .requestMatchers(HttpMethod.POST, "/api/retail/wishlist/add").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/retail/wishlist/remove").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/wishlist/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/retail/wishlist/clear/**").authenticated()

                                // ==================== RETAIL ORDER ENDPOINTS ====================
                                .requestMatchers(HttpMethod.POST, "/api/retail/orders/place").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/orders/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/orders/user/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/orders/status/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/retail/orders/**").authenticated()

                                // ==================== RETAIL COUPON ENDPOINTS ====================
                                .requestMatchers(HttpMethod.POST, "/api/retail/coupons/create").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/coupons/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/coupons/code/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/coupons/active/all").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/coupons/admin/all").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/retail/coupons/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/retail/coupons/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/retail/coupons/validate").permitAll()

                                // ==================== RETAIL HOMEPAGE ENDPOINTS ====================
                                .requestMatchers(HttpMethod.GET, "/api/retail/homepage/data").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/homepage/banners").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/homepage/offers").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/homepage/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/homepage/trending").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/homepage/latest").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/homepage/flash-deals").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/homepage/on-sale").permitAll()

                                // ==================== RETAIL CATEGORY ENDPOINTS ====================
                                .requestMatchers(HttpMethod.GET, "/api/retail/categories/all").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/categories/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/categories/hierarchy").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/categories/search/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/categories/popular").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/retail/categories/browse").permitAll()

                                // ==================== RETAIL REVIEW ENDPOINTS ====================
                                .requestMatchers(HttpMethod.POST, "/api/retail/reviews/create").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/reviews/product/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/retail/reviews/user/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/reviews/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/retail/reviews/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/retail/reviews/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/reviews/recent/all").permitAll()

                                // ==================== RETAIL ANALYTICS ENDPOINTS ====================
                                .requestMatchers(HttpMethod.GET, "/api/retail/analytics/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/analytics/dashboard/summary").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/analytics/sales/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/analytics/top-products").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/analytics/order-status-distribution").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/analytics/payment-distribution").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/analytics/inventory-status").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/retail/analytics/recent-orders").authenticated()

                                .requestMatchers(HttpMethod.GET).permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
