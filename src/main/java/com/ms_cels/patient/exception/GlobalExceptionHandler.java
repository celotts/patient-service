package com.ms_cels.patient.exception;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ✅ Captura errores de validación en DTO con @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Errores de validación detectados.");

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "campo", error.getField(),
                        "mensaje", Objects.requireNonNull(error.getDefaultMessage())
                ))
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Errores de validación", errors);
    }

    // ✅ Captura errores de validación en parámetros de URL (@RequestParam, @PathVariable, etc.)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.warn("Errores de validación en parámetros.");

        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(violation -> Map.of(
                        "campo", violation.getPropertyPath().toString(),
                        "mensaje", violation.getMessage()
                ))
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Errores de validación", errors);
    }

    // ✅ Captura errores de UUID mal formateados
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("UUID inválido detectado: {}", ex.getMessage());

        List<Map<String, String>> errors = List.of(Map.of(
                "campo", "id",
                "mensaje", "El ID proporcionado no tiene un formato válido de UUID."
        ));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Solicitud incorrecta", errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Extrae el mensaje real del error de la base de datos
        String rootMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        // Solo imprime un mensaje resumido, no el error completo
        logger.error("🔴 Violación de integridad de datos detectada");

        // 📌 Imprime solo el mensaje esencial sin el stack trace
        logger.error("📌 Mensaje de error: {}", rootMessage.split("\n")[0]);

        List<Map<String, String>> errors = extractAllErrorsFromMessage(rootMessage);

        if (errors.isEmpty()) {
            errors.add(Map.of(
                    "campo", "desconocido",
                    "mensaje", "Error de integridad de datos. Verifique que no haya valores duplicados o inválidos."
            ));
        }

        return buildErrorResponse(HttpStatus.CONFLICT, "Violación de restricciones", errors);
    }

    // ✅ Captura cualquier otra excepción y evita errores internos 500
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex, WebRequest request) {
        // Logging detallado
        logger.error("Excepción capturada: ", ex);  // Esto mostrará el stack trace completo
        logger.error("Tipo de excepción: {}", ex.getClass().getName());
        logger.error("Mensaje de excepción: {}", ex.getMessage());

        String path = request.getDescription(false);
        logger.error("Path de la solicitud: {}", path);

        if (path.contains("/actuator")) {
            throw new RuntimeException(ex);
        }

        List<Map<String, String>> errors = List.of(Map.of(
                "mensaje", "Ocurrió un error inesperado. Contacte con soporte."
        ));

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());

        List<Map<String, String>> errors = List.of(Map.of(
                "campo", "id",
                "mensaje", ex.getMessage()
        ));

        return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado", errors);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        // Si es una solicitud de favicon.ico, simplemente retorna 404 sin loguear como error
        if (ex.getMessage().contains("favicon.ico")) {
            return ResponseEntity.notFound().build();
        }

        // Para otros recursos no encontrados, seguimos el patrón normal de errores
        logger.warn("Recurso estático no encontrado: {}", ex.getMessage());

        List<Map<String, String>> errors = List.of(Map.of(
                "recurso", ex.getResourcePath(),
                "mensaje", "El recurso solicitado no existe"
        ));

        return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado", errors);
    }


    // ✅ Método para construir una respuesta con múltiples errores
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String errorType, List<Map<String, String>> errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", errorType);
        response.put("errors", errors);

        return new ResponseEntity<>(response, status);
    }

    private List<Map<String, String>> extractAllErrorsFromMessage(String message) {
        List<Map<String, String>> errors = new ArrayList<>();
        logger.error("🔍 Analizando mensaje de error: {}", message);

        // Verificar si es un error de check constraint
        Pattern checkPattern = Pattern.compile("violates check constraint \"(.+?)\"");
        Matcher checkMatcher = checkPattern.matcher(message);

        if (checkMatcher.find()) {
            String constraintName = checkMatcher.group(1);
            logger.error("🔍 Detectada restricción de check: {}. Mensaje completo: {}", constraintName, message);

            // Mapeo de nombres de restricciones a campos y mensajes amigables
            Map<String, String> fieldMap = new HashMap<>();
            fieldMap.put("patients_first_name_check", "firstName");
            fieldMap.put("patients_last_name_check", "lastName");
            // Añade más mapeos según necesites

            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("patients_first_name_check",
                    "El formato del nombre es incorrecto. Debe contener solo letras, espacios, puntos y guiones. Ejemplo válido: 'Carlos E.'");
            messageMap.put("patients_last_name_check",
                    "El formato del apellido es incorrecto. Debe contener solo letras, espacios, puntos y guiones. Ejemplo válido: 'Lott S.'");
            // Añade más mensajes según necesites

            String field = fieldMap.getOrDefault(constraintName, "desconocido");
            String errorMessage = messageMap.getOrDefault(constraintName, "El valor no cumple con las restricciones establecidas.");

            Map<String, String> error = new HashMap<>();
            error.put("campo", field);
            error.put("mensaje", errorMessage);

            // Añadir valor específico si es posible extraerlo
            if (message.contains("value") && constraintName.contains("check")) {
                Pattern valuePattern = Pattern.compile("value=\\(([^)]*)\\)");
                Matcher valueMatcher = valuePattern.matcher(message);
                if (valueMatcher.find()) {
                    String value = valueMatcher.group(1);
                    error.put("valor", value);
                }
            }

            errors.add(error);
            return errors;
        }

        // MÉTODO 1: Extraer directamente el campo del mensaje de error (para unique constraints)
        String directFieldName = extractFieldNameFromMessage(message);
        if (directFieldName != null && !directFieldName.isEmpty()) {
            logger.error("✅ Campo detectado directamente: {}", directFieldName);

            String duplicateValue = extractDuplicateValue(message, directFieldName);

            Map<String, String> error = new HashMap<>();
            error.put("campo", mapDBFieldToDTO(directFieldName));
            error.put("mensaje", "El valor ya existe en la base de datos.");

            if (!"Valor no identificado".equals(duplicateValue)) {
                error.put("valor", duplicateValue);
            }

            errors.add(error);
            return errors;
        }

        // MÉTODO 2: Análisis de la restricción unique
        Pattern uniquePattern = Pattern.compile("violates unique constraint \"(.+?)\"");
        Matcher uniqueMatcher = uniquePattern.matcher(message);

        while (uniqueMatcher.find()) {
            String constraintName = uniqueMatcher.group(1);
            String field = constraintToField(constraintName);
            String duplicateValue = extractDuplicateValue(message, field);

            logger.error("🔍 Campo determinado desde restricción: {}", field);

            if (!"desconocido".equals(field) && !"Valor no identificado".equals(duplicateValue)) {
                errors.add(Map.of(
                        "campo", field,
                        "valor", duplicateValue,
                        "mensaje", "El valor ya existe en la base de datos."
                ));
            } else {
                errors.add(Map.of(
                        "campo", field,
                        "mensaje", "El valor ya existe en la base de datos."
                ));
            }
        }

        // Si no se identifica ningún tipo de error conocido
        if (errors.isEmpty()) {
            errors.add(Map.of(
                    "campo", "desconocido",
                    "mensaje", "Se ha producido un error al procesar los datos. Por favor, verifique la información e intente nuevamente."
            ));
        }

        return errors;
    }

    // Método auxiliar para mapear nombres de campos de la BD a nombres de propiedades del DTO
    private String mapDBFieldToDTO(String dbField) {
        // Si ya tiene el formato correcto, devolverlo
        if (!dbField.contains("_") && !dbField.startsWith("patients_")) {
            return dbField;
        }

        // Eliminar el prefijo "patients_" si existe
        if (dbField.startsWith("patients_")) {
            dbField = dbField.substring("patients_".length());
        }

        // Convertir snake_case a camelCase
        if (dbField.contains("_")) {
            StringBuilder result = new StringBuilder();
            boolean capitalizeNext = false;

            for (char c : dbField.toCharArray()) {
                if (c == '_') {
                    capitalizeNext = true;
                } else {
                    if (capitalizeNext) {
                        result.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        result.append(c);
                    }
                }
            }

            return result.toString();
        }

        return dbField;
    }

    private String extractFieldNameFromMessage(String message) {
        if (message == null) return null;

        // Patrón para capturar nombres de columnas en violaciones de clave única
        Pattern uniqueConstraintPattern = Pattern.compile("Key \\((.*?)\\)=");
        Matcher matcher = uniqueConstraintPattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1); // Devuelve el nombre del campo que causa el error
        }

        // Patrón para capturar violaciones de clave foránea
        Pattern foreignKeyPattern = Pattern.compile("constraint \"(.*?)\"");
        matcher = foreignKeyPattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1); // Devuelve el nombre de la restricción (puedes mapearlo a un campo si lo necesitas)
        }

        return null; // Si no encuentra coincidencias, devuelve null
    }

    /**
     * 🚀 ✅ Convierte el nombre de la restricción en el campo correspondiente
     */
    private String constraintToField(String constraintName) {
        logger.error("🔍 Analizando restricción única: {}", constraintName);

        if (constraintName.equals("patients_email_key")) {
            return "email";
        } else if (constraintName.equals("patients_insurance_number_key")) {
            return "insuranceNumber";
        } else if (constraintName.contains("_key")) {
            return constraintName.replace("patients_", "").replace("_key", "");
        }
        return "desconocido"; // Si no se puede detectar, marcar como desconocido
    }

    private String extractDuplicateValue(String message, String field) {
        if (field == null || field.equals("desconocido")) return "Valor no identificado";

        logger.error("🔍 Mensaje de error ORIGINAL de la BD: [{}]", message);
        logger.error("🔍 Buscando valor duplicado para el campo: {}", field);

        // Expresión regular mejorada para capturar el valor exacto después de Key (field)=(...)
        Pattern pattern = Pattern.compile("Key \\(" + field + "\\)=\\((.*?)\\)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String duplicatedValue = matcher.group(1).trim();
            if (!duplicatedValue.isEmpty()) {
                logger.error("✅ Valor duplicado encontrado: [{}]", duplicatedValue);

                // Alerta en una sola línea para números de seguro
                if (field.equals("insuranceNumber") || field.equals("insurance_number")) {
                    logger.error("⚠️ ALERTA: Número de seguro duplicado: {} - Por favor, use un número diferente", duplicatedValue);
                }
// Alerta para teléfonos duplicados
                else if (field.equals("phone")) {
                    logger.error("⚠️ ALERTA: Número de teléfono duplicado: {} - Por favor, use un número diferente", duplicatedValue);
                }

                return duplicatedValue;
            }
        }

        // Segundo intento: Buscar manualmente en el mensaje de error
        int index = message.indexOf("Key (" + field + ")=");
        if (index != -1) {
            int start = message.indexOf("(", index) + 1;
            int end = message.indexOf(")", start);
            if (start > 0 && end > start) {
                String potentialValue = message.substring(start, end).trim();
                logger.error("🔍 Valor duplicado detectado manualmente: [{}]", potentialValue);

                // Alerta en una sola línea (segundo intento)
                if (field.equals("insuranceNumber") || field.equals("insurance_number")) {
                    logger.error("⚠️ ALERTA: Número de seguro duplicado: {} - Por favor, use un número diferente", potentialValue);
                }

                return potentialValue;
            }
        }

        // Tercer intento: Capturar cualquier valor entre paréntesis después de "Key"
        Pattern genericPattern = Pattern.compile("Key \\(.*?\\)=\\((.*?)\\)");
        Matcher genericMatcher = genericPattern.matcher(message);
        if (genericMatcher.find()) {
            String genericValue = genericMatcher.group(1).trim();
            if (!genericValue.isEmpty()) {
                logger.error("✅ Valor duplicado encontrado (Fallback): [{}]", genericValue);
                return genericValue;
            }
        }

        logger.error("⚠️ No se pudo extraer el valor duplicado. Mensaje de error recibido: [{}]", message);
        return "Valor no identificado";
    }
}