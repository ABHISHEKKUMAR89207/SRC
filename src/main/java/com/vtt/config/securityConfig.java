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
                .cors(AbstractHttpConfigurer::disable)
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
                                .requestMatchers("/v3/api-docs").permitAll()
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

                                .requestMatchers("/api/user/**").permitAll()
                                .requestMatchers("/api/combos/**").permitAll()
                                .requestMatchers("/api/cart/**").permitAll()
                                .requestMatchers("/api//category/**").permitAll()
                                .requestMatchers("/offers/**").permitAll()
                                .requestMatchers("/api/payment/**").permitAll()
                                .requestMatchers("/api/orders/**").permitAll()
                                .requestMatchers("/api/productinventory/**").permitAll()
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
