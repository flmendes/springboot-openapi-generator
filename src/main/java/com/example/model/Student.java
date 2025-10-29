package com.example.model;

import java.time.LocalDateTime;

/**
 * Immutable domain model representing a student entity.
 * <p>
 * This is a Java record (introduced in Java 14, finalized in Java 16) that provides
 * a concise way to declare immutable data carriers. Records automatically generate:
 * </p>
 * <ul>
 *   <li>Constructor with all fields</li>
 *   <li>Accessor methods (id(), name(), email(), phone(), createdAt())</li>
 *   <li>equals(), hashCode(), and toString() methods</li>
 * </ul>
 * <p>
 * The Student record is used internally in the service layer and is separate from
 * the API DTOs (StudentRequest/StudentResponse) to maintain separation between
 * domain models and API contracts.
 * </p>
 *
 * @param id unique identifier for the student, typically a UUID
 * @param name full name of the student
 * @param email email address of the student (must be unique)
 * @param phone phone number of the student
 * @param createdAt timestamp when the student record was created
 *
 * @author Spring Boot OpenAPI Generator
 * @version 1.0
 * @since 1.0
 * @see java.lang.Record
 */
public record Student(String id, String name, String email, String phone, LocalDateTime createdAt) {

    /**
     * Factory method to create a new Student instance with auto-generated ID and timestamp.
     * <p>
     * This factory method simplifies student creation by automatically generating:
     * </p>
     * <ul>
     *   <li>A unique UUID as the student ID</li>
     *   <li>The current timestamp for createdAt</li>
     * </ul>
     * <p>
     * This ensures that all students have consistent ID generation and creation timestamps.
     * </p>
     *
     * @param name the full name of the student (required)
     * @param email the email address of the student (required, must be unique)
     * @param phone the phone number of the student (required)
     * @return a new Student instance with generated ID and current timestamp
     * @throws NullPointerException if any parameter is null
     */
    public static Student create(String name, String email, String phone) {
        return new Student(
            java.util.UUID.randomUUID().toString(),
            name,
            email,
            phone,
            LocalDateTime.now()
        );
    }
}
