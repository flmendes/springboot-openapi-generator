package com.example.dto;

import com.example.model.Student;
import java.time.LocalDateTime;

public record StudentResponse(
    String id,
    String name,
    String email,
    String phone,
    LocalDateTime createdAt
) {
    // Factory method to create response from Student entity
    public static StudentResponse fromStudent(Student student) {
        return new StudentResponse(
            student.id(),
            student.name(),
            student.email(),
            student.phone(),
            student.createdAt()
        );
    }
}
