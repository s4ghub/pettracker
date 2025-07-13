package com.example.pettracker.pettracker.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * All the thrown exceptions are handled by this class. Debugging becomes much easier and hence
 * we don't need to look into other places to find how the Exceptions are handled.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorCodes.ERRORCODE7, ErrorCodes.ERRORCODE7MESSAGE);
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse response = new ErrorResponse(ErrorCodes.ERRORCODE7, "Validation failed", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorCodes.ERRORCODE2, ErrorCodes.ERRORCODE2MESSAGE);
        ErrorResponse response = new ErrorResponse(ErrorCodes.ERRORCODE2, ex.getMostSpecificCause().getMessage(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaduserInputException.class)
    public ResponseEntity<ErrorResponse> handleBadInputException(BaduserInputException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorCodes.ERRORCODE1, ErrorCodes.ERRORCODE1MESSAGE);
        ErrorResponse response = new ErrorResponse(ErrorCodes.ERRORCODE1, ex.getMessage(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * For General purpose Exception handler in case all other above handlers do not catch.
     * @param ex exception
     * @return ResponseEntity
     */
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorCodes.ERRORCODE3, ErrorCodes.ERRORCODE3MESSAGE);
        ErrorResponse response = new ErrorResponse(ErrorCodes.ERRORCODE3, ex.getMessage(), errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
