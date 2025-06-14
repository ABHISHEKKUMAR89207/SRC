package com.vtt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
//                        "http://68.183.89.215:5000",
                        "http://vastratreasuretrove.shop",
                        "https://vastratreasuretrove.shop",
                        "http://localhost:3000",
                        "http://195.35.20.130:3000"
//                        "http://142.93.221.34:2026",
//                        "http://142.93.221.34:1552",
//                        "http://142.93.221.34:1555/api/serial-products"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
