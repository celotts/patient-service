package com.ms_cels.patient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // Configuración de usuario en memoria para pruebas
        return unused -> User.builder()
                .username("user")
                .password(passwordEncoder().encode("password")) // Usando BCrypt para la contraseña
                .roles("USER")
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Configuración moderna para deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v1/patients/**",  // Permitir acceso a las rutas de pacientes
                                "/v3/api-docs/**", // Permitir acceso a la documentación de OpenAPI
                                "/swagger-ui/**"  // Permitir acceso a Swagger UI
                        ).permitAll() // Permitir acceso a Swagger
                        .anyRequest().permitAll()  // Requerir autenticación para las demás rutas
                )
                .httpBasic(Customizer.withDefaults());  // Autenticación básica

        return http.build();  // Construir la configuración de seguridad
    }
}