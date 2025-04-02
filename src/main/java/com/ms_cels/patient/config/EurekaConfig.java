package com.ms_cels.patient.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EurekaConfig {

    private static final Logger log = LoggerFactory.getLogger(EurekaConfig.class);

    private final DiscoveryClient discoveryClient;

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaDefaultZone;

    public EurekaConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @PostConstruct
    public void updateEurekaUrl() {
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("registry-server");

            if (!instances.isEmpty()) {
                String newEurekaUrl = instances.get(0).getUri().toString() + "/eureka/";
                System.setProperty("eureka.client.service-url.defaultZone", newEurekaUrl);
                log.info("Eureka Server detectado en: {}", newEurekaUrl);
            } else {
                log.info("Usando la URL de Eureka configurada por defecto: {}", eurekaDefaultZone);
            }
        } catch (Exception e) {
            log.warn("Error al intentar actualizar la URL de Eureka: {}", e.getMessage());
        }
    }
}