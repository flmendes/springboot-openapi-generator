package com.example.service;

import com.example.model.Student;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.students.model.StudentRequest;
import com.example.students.model.StudentResponse;
import org.springframework.stereotype.Service;


@Service
public class StudentService {

    private final Map<String, Student> students = new ConcurrentHashMap<>();

    public StudentResponse createStudent(StudentRequest request) {

        if (emailExists(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new student using factory method
        Student student = Student.create(request.getName(), request.getEmail(), request.getPhone());

        // Save to memory
        students.put(student.id(), student);

        // Return response using factory method
        return toStudentResponse(student);
    }

    private StudentResponse toStudentResponse(Student student) {
        var response = new StudentResponse();
        response.setId(student.id());
        response.setName(student.name());
        response.setEmail(student.email());
        response.setPhone(student.phone());
        return response;
    }

    public int getStudentCount() {
        return students.size();
    }

    private boolean emailExists(String email) {
        return students
            .values()
            .stream()
            .anyMatch(student -> student.email().equals(email));
    }
}
