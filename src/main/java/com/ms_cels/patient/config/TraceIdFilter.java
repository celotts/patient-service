package com.ms_cels.patient.config;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Genera un UUID único como traceId
        String traceId = UUID.randomUUID().toString();

        // Establece el traceId en el MDC
        MDC.put("traceId", traceId);

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);

        // Limpiar el MDC al finalizar la solicitud
        MDC.remove("traceId");
    }
}