package com.ms_cels.patient.config;

import jakarta.annotation.PostConstruct;
import org.hibernate.internal.util.config.ConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class DatabaseConfigValidator {

    @Value("${spring.datasource.hikari.connection-timeout:30000}")
    private Long connectionTimeout;

    @PostConstruct
    public void validateConfig() {
        if (connectionTimeout == null) {
            throw new ConfigurationException("No se pudo resolver el valor de connection-timeout");
        }

        // Puedes añadir más validaciones según sea necesario
        // Por ejemplo:

        /*
        @Value("${datasource.url}")
        private String databaseUrl;

        if (databaseUrl == null || databaseUrl.trim().isEmpty()) {
            throw new ConfigurationException("La URL de la base de datos no está configurada");
        }
        */
    }
}
