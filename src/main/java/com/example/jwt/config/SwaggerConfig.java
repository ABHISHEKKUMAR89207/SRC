package com.example.jwt.config;//package com.example.jwt.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//    public static final String AUTHORIZATION_HEADER="Authorization";
//
//    private ApiKey apiKey()
//    {
//        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
//    }
//
//    private List<SecurityContext> securityContexts()
//    {
//        return Arrays.asList(SecurityContext.builder().securityReferences(sf()).build());
//        //return SecurityContext.builder().securityReferences(null).build();
//    }
//
//    private List<SecurityReference> sf()
//    {
//        AuthorizationScope scope=new AuthorizationScope("global","accessEverything");
//        return Arrays.asList(new SecurityReference("JWT",new AuthorizationScope[]{scope}));
//    }
//
//    @Bean
//    public Docket api(){
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.jwt.controller")) // Replace with your controller package
//                .paths(PathSelectors.any())
//                .build();
//
////        return new Docket(DocumentationType.SWAGGER_2)
////                .apiInfo(getInfo())
////                .securityContexts(securityContexts())
////                .securitySchemes(Arrays.asList(apiKey()))
////                .select()
////                .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.any()).build();
//    }
//
//    private ApiInfo getInfo(){
//
//        return new ApiInfo("Blogging Application : Backend Course",
//                "This Project is Developed By Raj","1.0","Terms of Service",
//                new Contact("Raj","https://SoftechStudy.com","raj.secpnd344@gmail.com"),
//                "License of APIS","API license URL", Collections.emptyList());
//
//    };
//
//
//}
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.jwt.controller")) // Replace with your controller package
//                .paths(PathSelectors.any())
//                .build();
//    }
//}
//
//
//
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.VendorExtension;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//
//import static springfox.documentation.builders.PathSelectors.regex;
//
//import java.util.ArrayList;
//
//@Configuration
//@EnableSwagger2
//@EnableWebMvc
//public class SwaggerConfig implements WebMvcConfigurer {
//
//    @Bean
//    public Docket atividadeApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.jwt"))
//                .paths(PathSelectors.regex("/.*"))
//                .build()
//                .apiInfo(metaInfo());
//    }
//
//    private ApiInfo metaInfo() {
//
////        ApiInfo apiInfo = new ApiInfo(
////                "Atividades API REST",
////                "API REST de cadastro de atividades.",
////                "1.0",
////                "Terms of Service",
////                new Contact("João VR", "www.una.br/",
////                        " "),
////                "Apache License Version 2.0",
////                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
//
////        );
//
//        return new ApiInfoBuilder().title("NIN")
//                .description("Api EndPoint Decoration")
//                .contact(new Contact("Dev-Team","https://www.dev-team.com/","rajlumariimt2002@gmail.com"))
//                .license("Apache 2.0")
//                .licenseUrl("https://www.apache.org/licesen.html")
//                .version("1.0,0")
//                .build();
//
////        return apiInfo;
//    }
//}
@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){

       return new OpenAPI()
               .info(new Info()
                       .title("NIN APIs")
                       .description("This is ni developed by o2i")
                       .version("1.0")
                       .contact(new Contact().name("Raj").email("raj@gmail.com").url("raj.com"))
                       .license(new License().name("Apache"))

               )
               .externalDocs(new ExternalDocumentation().url("raj.com").description("this is external url"))

               ;
    }


//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.jwt.controller.FoodTodayController"))
//                .paths(PathSelectors.any())
//                .build();
//    }
}