package com.cloudcomp.students.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Estudiantes")
                        .version("1.0")
                        .description("API RESTful para la gestión de estudiantes")
                        .contact(new Contact()
                                .name("Grupo 1")
                                .email("fabio.davila@utec.edu.pe")
                                .url("https://github.com/fdavilaventuro/Students-Backend")));
    }
}