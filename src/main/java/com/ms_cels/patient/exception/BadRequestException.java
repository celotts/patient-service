package com.ms_cels.patient.exception;

public class BadRequestException extends RuntimeException { // Asegúrate de extender de RuntimeException
    public BadRequestException(String message) {
        super(message);
    }
}