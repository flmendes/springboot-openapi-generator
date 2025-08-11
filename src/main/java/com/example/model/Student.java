package com.example.model;

import java.time.LocalDateTime;

public record Student(String id, String name, String email, String phone, LocalDateTime createdAt) {
    // Factory method to create a new student with generated ID and timestamp
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
