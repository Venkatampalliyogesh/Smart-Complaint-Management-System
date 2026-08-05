package com.scms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        private static final String SECURITY_SCHEME = "Bearer Authentication";

        @Bean
        public OpenAPI openAPI() {

                return new OpenAPI()

                                .info(new Info()
                                                .title("Smart Complaint Management System API")
                                                .version("1.0.0")
                                                .description("REST API Documentation for Smart Complaint Management System")
                                                .contact(new Contact()
                                                                .name("SCMS Development Team")
                                                                .email("support@scms.com"))
                                                .license(new License()
                                                                .name("Apache License 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0")))

                                .addSecurityItem(
                                                new SecurityRequirement()
                                                                .addList(SECURITY_SCHEME))

                                .components(
                                                new Components()
                                                                .addSecuritySchemes(
                                                                                SECURITY_SCHEME,
                                                                                new SecurityScheme()
                                                                                                .name("Authorization")
                                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                                .scheme("bearer")
                                                                                                .bearerFormat("JWT")));
        }
}