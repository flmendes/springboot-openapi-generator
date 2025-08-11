package com.example.service;

import com.example.dto.StudentRequest;
import com.example.dto.StudentResponse;
import com.example.model.Student;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final Map<String, Student> students = new ConcurrentHashMap<>();

    public StudentResponse createStudent(StudentRequest request) {
        // Check if email already exists
        boolean emailExists = students
            .values()
            .stream()
            .anyMatch(student -> student.email().equals(request.email()));

        if (emailExists) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new student using factory method
        Student student = Student.create(request.name(), request.email(), request.phone());

        // Save to memory
        students.put(student.id(), student);

        // Return response using factory method
        return StudentResponse.fromStudent(student);
    }

    public int getStudentCount() {
        return students.size();
    }

    public boolean emailExists(String email) {
        return students
            .values()
            .stream()
            .anyMatch(student -> student.email().equals(email));
    }
}
