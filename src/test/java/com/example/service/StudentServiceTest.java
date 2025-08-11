package com.example.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.model.Student;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class StudentRecordTest {

    @Test
    void createStudent_ShouldGenerateIdAndTimestamp() {
        Student student = Student.create("John Doe", "john@email.com", "(11) 99999-9999");

        assertNotNull(student.id());
        assertEquals("John Doe", student.name());
        assertEquals("john@email.com", student.email());
        assertEquals("(11) 99999-9999", student.phone());
        assertNotNull(student.createdAt());
        assertTrue(student.createdAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void studentRecord_ShouldBeImmutable() {
        Student student = new Student(
                "123",
                "John Doe",
                "john@email.com",
                "(11) 99999-9999",
                LocalDateTime.now()
        );

        // Records are immutable by default - no setters exist
        // This test verifies the record structure
        assertEquals("123", student.id());
        assertEquals("John Doe", student.name());
        assertEquals("john@email.com", student.email());
        assertEquals("(11) 99999-9999", student.phone());
        assertNotNull(student.createdAt());
    }

    @Test
    void studentRecord_ShouldImplementEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        Student student1 = new Student("123", "John", "john@email.com", "111", now);
        Student student2 = new Student("123", "John", "john@email.com", "111", now);
        Student student3 = new Student("456", "Jane", "jane@email.com", "222", now);

        assertEquals(student1, student2);
        assertNotEquals(student1, student3);
        assertEquals(student1.hashCode(), student2.hashCode());
    }
}