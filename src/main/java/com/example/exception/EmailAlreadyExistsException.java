package com.example.exception;

/**
 * Exception thrown when attempting to create a student with an email that already exists.
 * <p>
 * This is a business logic exception that indicates a violation of the unique email
 * constraint. It extends {@link RuntimeException}, making it an unchecked exception
 * that doesn't require explicit declaration in method signatures.
 * </p>
 * <p>
 * When this exception is thrown, the {@link GlobalExceptionHandler} catches it and
 * returns an RFC 7807 Problem Details response with HTTP status 409 (Conflict).
 * </p>
 *
 * @author Spring Boot OpenAPI Generator
 * @version 1.0
 * @since 1.0
 * @see GlobalExceptionHandler#handleEmailAlreadyExists(EmailAlreadyExistsException)
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new EmailAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}