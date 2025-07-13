package com.example.pettracker.pettracker.exceptionhandling;

import lombok.Data;

import java.util.Map;

/**
 * This class encapsulates all the error messages
 */
@Data
public class ErrorResponse {
    private String status;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(String status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}
