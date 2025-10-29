package com.example.controller;

import com.example.service.StudentService;
import com.example.students.api.StudentsApi;
import com.example.students.model.StudentRequest;
import com.example.students.model.StudentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing student resources.
 * <p>
 * This controller implements the OpenAPI-generated {@link StudentsApi} interface,
 * providing endpoints for student management operations. It follows the API-first
 * development approach where the API contract is defined in OpenAPI specification
 * and the implementation is derived from it.
 * </p>
 * <p>
 * All validation is performed automatically by Spring's validation framework
 * based on the constraints defined in the OpenAPI specification.
 * Error responses follow RFC 7807 Problem Details format.
 * </p>
 *
 * @author Spring Boot OpenAPI Generator
 * @version 1.0
 * @since 1.0
 * @see StudentsApi
 * @see StudentService
 */
@RestController
public class StudentController implements StudentsApi {

    /**
     * Service layer for student business logic.
     */
    private final StudentService studentService;

    /**
     * Constructs a new StudentController with the given service.
     * <p>
     * Constructor-based dependency injection is used following Spring best practices.
     * </p>
     *
     * @param studentService the student service for business logic operations
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Creates a new student resource.
     * <p>
     * This method implements the POST /students endpoint defined in the OpenAPI specification.
     * It validates the incoming request, creates a new student, and returns the created resource.
     * Validation errors return HTTP 400, duplicate emails return HTTP 409.
     * </p>
     *
     * @param request the student creation request containing name, email, and phone
     * @return ResponseEntity with HTTP 201 (Created) status and the created student details
     * @throws com.example.exception.EmailAlreadyExistsException if the email already exists (returns HTTP 409)
     */
    @Override
    public ResponseEntity<StudentResponse> studentsPost(StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}