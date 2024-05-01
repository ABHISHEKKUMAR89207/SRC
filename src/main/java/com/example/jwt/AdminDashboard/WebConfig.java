package com.example.jwt.AdminDashboard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Value("${image.basePath}")
//    private String basePath; // Base path for images

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/")
//                .setCachePeriod(0); // Disable caching for development
//
////        registry.addResourceHandler("/img/**")
////                .addResourceLocations("classpath:/static/img/")
////                .setCachePeriod(0); // Disable caching for development
////    }
//        registry.addResourceHandler("/static/**", "/img/**")
//                .addResourceLocations("classpath:/static/");
//    }

    @Bean
    public GeocodingUtility geocodingUtility() {
        return new GeocodingUtility();
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("classpath:/static/images/");
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("classpath:./images/")
//                .setCachePeriod(0); // Disable caching for testing
//    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./images/")
                .setCachePeriod(3600) // Cache period in seconds
                .resourceChain(true);
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("file:./images/")
////                .addResourceLocations("file:" + basePath)
//                .setCachePeriod(0); // Disable caching for development
//    }


//    @Value("${image.basePath}")
//    private String basePath; // Base path for images
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**")
////                .addResourceLocations("file:./images/")
//                .addResourceLocations("file:" + basePath) // Serving images from the configured base path
//                .setCachePeriod(3600); // Cache period in seconds
//    }
//@Override
//public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry.addResourceHandler("/images/**", "/rowIngImage/**")
//            .addResourceLocations("file:./images/", "file:./rowIngImage/")
//            .setCachePeriod(3600) // Cache period in seconds
//            .resourceChain(true);
//}

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("file:" + basePath) // Serving images from the configured base path
//                .setCachePeriod(3600); // Cache period in seconds
//    }

}

