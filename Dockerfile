FROM openjdk:17-jdk-slim

WORKDIR /app

# Instalar herramientas necesarias
RUN apt update && apt install -y curl

# Copiar wait-for-it script
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# Copiar el JAR
COPY target/*.jar app.jar

# Puerto
EXPOSE 8081

# Script para esperar a que Eureka esté activo y luego iniciar con opciones para deshabilitar métricas
ENTRYPOINT ["/bin/sh", "-c", "/app/wait-for-it.sh registry-server:8762 --timeout=30 --strict -- java -Dio.micrometer.system-metrics.enabled=false -Dmanagement.metrics.enable.system=false -Dmanagement.metrics.enable.process=false -Dspring.autoconfigure.exclude=org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration,org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration -jar app.jar"]