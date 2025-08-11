package com.example.controller;

import com.example.service.StudentService;
import com.example.students.api.StudentsApi;
import com.example.students.model.StudentRequest;
import com.example.students.model.StudentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StudentController implements StudentsApi {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public ResponseEntity<StudentResponse> studentsPost(StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}