package com.ms_cels.patient.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Patient Crud API",
                version = "1.0.0",
                description = "This Documentation Patient API v1.0.0"
        )
)
public class OpenApiConfig {
}
