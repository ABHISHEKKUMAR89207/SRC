// Security Configuration file for each API 

package com.example.jwt.config;

import com.example.jwt.security.JwtAuthenticationEntryPoint;
import com.example.jwt.security.JwtAuthenticationFilter;
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
                                .requestMatchers("/auth/refresh-token").permitAll()
                                .requestMatchers("/auth/health-trends/**").authenticated()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/v3/api-docs").permitAll()
                                .requestMatchers("/api/sleep-logs").permitAll()
                                .requestMatchers("/v3/api-docs/creatSleepLog").permitAll()
                                .requestMatchers("/logs").permitAll()
                                .requestMatchers("/ingredient/uploadImages").permitAll()
                                .requestMatchers("/api").permitAll()
                                .requestMatchers("/api/food/add").permitAll()
                                .requestMatchers("/authh/google-loginn").permitAll()
                                .requestMatchers("/downloadLog").permitAll()
                                .requestMatchers("/v3/api-docs").permitAll()
                                .requestMatchers("/api/sleep-logs").permitAll()
                                .requestMatchers("/v3/api-docs/creatSleepLog").permitAll()
                                .requestMatchers("/dashboard/**").permitAll()
                                .requestMatchers("/dashboard/send-email").permitAll()
                                .requestMatchers("/dashboard//contactUs/viewImage/{imageName}").permitAll()
//                                .requestMatchers("/dashboard/dashboard.html").authenticated()
                                .requestMatchers("/api/water/intake").permitAll()
                                .requestMatchers("/api/activity-types/get-all-occupations").permitAll()
                                .requestMatchers("/api/profile/get-all-ear").permitAll()
                                .requestMatchers("/recipes/all").permitAll()
                                .requestMatchers("/foods/ing-details").permitAll()
                                .requestMatchers("/dashboard/user-gender-count").permitAll()
                                .requestMatchers("/dashboard/user-login").permitAll()
                                .requestMatchers("/dashboard/all").permitAll()
                                .requestMatchers("/api/booktables/{id}").permitAll()
                                .requestMatchers("/api/food/all-row-food").permitAll()

                                .requestMatchers("/api/booktables/**").permitAll()
                                .requestMatchers("/api/contact-us/**").permitAll()
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
