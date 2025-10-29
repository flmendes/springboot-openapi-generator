package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Global exception handler for all REST controllers in the application.
 * <p>
 * This class provides centralized exception handling across all {@code @RestController}
 * annotated classes using Spring's {@code @RestControllerAdvice}. It implements
 * RFC 7807 Problem Details for HTTP APIs, providing standardized, machine-readable
 * error responses.
 * </p>
 * <p>
 * All error responses follow the RFC 7807 format and include:
 * </p>
 * <ul>
 *   <li><b>type</b> - URI identifying the problem type</li>
 *   <li><b>title</b> - Short, human-readable summary</li>
 *   <li><b>status</b> - HTTP status code</li>
 *   <li><b>detail</b> - Human-readable explanation specific to this occurrence</li>
 *   <li><b>instance</b> - URI reference identifying the specific occurrence</li>
 * </ul>
 * <p>
 * The handler manages the following exception types:
 * </p>
 * <ul>
 *   <li>Validation errors (400 Bad Request)</li>
 *   <li>Business rule violations like duplicate emails (409 Conflict)</li>
 *   <li>Invalid arguments (400 Bad Request)</li>
 *   <li>Unexpected server errors (500 Internal Server Error)</li>
 * </ul>
 *
 * @author Spring Boot OpenAPI Generator
 * @version 1.0
 * @since 1.0
 * @see ProblemDetail
 * @see RestControllerAdvice
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807 - Problem Details for HTTP APIs</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors for invalid request data.
     * Returns RFC 7807 Problem Details response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Validation failed for one or more fields"
        );

        problemDetail.setType(URI.create("https://api.example.com/errors/validation-error"));
        problemDetail.setTitle("Validation Error");

        // Collect all validation errors
        String errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Handles email already exists business rule violation.
     * Returns RFC 7807 Problem Details response.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            ex.getMessage()
        );

        problemDetail.setType(URI.create("https://api.example.com/errors/email-already-exists"));
        problemDetail.setTitle("Email Already Exists");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    /**
     * Handles illegal argument exceptions.
     * Returns RFC 7807 Problem Details response.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );

        problemDetail.setType(URI.create("https://api.example.com/errors/invalid-argument"));
        problemDetail.setTitle("Invalid Argument");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Generic exception handler for unexpected errors.
     * Returns RFC 7807 Problem Details response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleServerError(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred. Please try again later."
        );

        problemDetail.setType(URI.create("https://api.example.com/errors/internal-server-error"));
        problemDetail.setTitle("Internal Server Error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}