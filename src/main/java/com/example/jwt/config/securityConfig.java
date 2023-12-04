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
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebSecurity
@EnableWebMvc
//@EnableSwagger2
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


//    public static final String[] PUBLIC_URLS={
//            "/api/v1/auth/**",
//            "/api/v2/auth/**",
//            "/register/**",
//            "/auth/login",
////            "/auth/foodToday/**",
//
//            "/v3/api-docs",
//            "/v2/api-docs",
//            "/swagger-resources/**",
//            "/swagger-ui/**",
//            "/webjars/**"
//
//
//    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                .authorizeRequests().
        .authorizeHttpRequests(auth ->
                auth.requestMatchers("/home/**")
                        .authenticated()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/health-trends/**").authenticated()
//                        .requestMatchers("/auth/Dishes").authenticated()
                        .requestMatchers("/register/**").permitAll()
                        .requestMatchers("/v3/api-docs").permitAll()
                        .requestMatchers("/api/sleep-logs").permitAll()
                        .requestMatchers("/v3/api-docs/creatSleepLog").permitAll()

//                        .requestMatchers("/api/csv-export/users/export").permitAll()
//                        .requestMatchers(PUBLIC_URLS).permitAll()
//                        .requestMatchers(HttpMethod.POST).permitAll()

                        .requestMatchers("/Logs").permitAll()
                        .requestMatchers("/api/booktables/{id}").permitAll()
                        .requestMatchers("/api/booktables").permitAll()

                        .requestMatchers(HttpMethod.GET).permitAll()
//                        .requestMatchers("/auth/create-user").permitAll()
//                        .requestMatchers(HttpMethod.PUT).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex->ex.authenticationEntryPoint(point))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
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
