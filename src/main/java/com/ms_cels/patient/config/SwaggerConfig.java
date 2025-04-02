package com.ms_cels.patient.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Medical Appointment - Patient Microservice API")
                        .version("1.0")
                        .description("API para la gestión de pacientes en el sistema de citas médicas")
                        .contact(new Contact()
                                .name("Soporte AppointMD")
                                .email("support@appointmd.com")
                                .url("https://appointmd.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}