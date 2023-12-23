package com.example.jwt.AdminDashboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0); // Disable caching for development

//        registry.addResourceHandler("/img/**")
//                .addResourceLocations("classpath:/static/img/")
//                .setCachePeriod(0); // Disable caching for development
//    }
        registry.addResourceHandler("/static/**", "/img/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public GeocodingUtility geocodingUtility() {
        return new GeocodingUtility();
    }
}

