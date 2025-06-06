// Auto Generation Documentation for API. 

package com.vtt.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("NIN APIs")
                        .description("This is ni developed by o2i")
                        .version("1.0")
                        .contact(new Contact().name("Raj").email("raj@gmail.com").url("raj.com"))
                        .license(new License().name("Apache"))
                )
                .externalDocs(new ExternalDocumentation().url("raj.com").description("this is external url"));
    }
}