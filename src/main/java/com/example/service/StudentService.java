package com.example.service;

import com.example.exception.EmailAlreadyExistsException;
import com.example.model.Student;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.students.model.StudentRequest;
import com.example.students.model.StudentResponse;
import org.springframework.stereotype.Service;

/**
 * Service class for managing student business logic.
 * <p>
 * This service provides the core business logic for student management operations,
 * including creation, validation, and data transformation. It uses an in-memory
 * thread-safe storage implementation using {@link ConcurrentHashMap}.
 * </p>
 * <p>
 * Key responsibilities:
 * </p>
 * <ul>
 *   <li>Student creation with duplicate email validation</li>
 *   <li>Transformation between domain models and API DTOs</li>
 *   <li>Business rule enforcement (e.g., unique email constraint)</li>
 * </ul>
 *
 * @author Spring Boot OpenAPI Generator
 * @version 1.0
 * @since 1.0
 * @see Student
 * @see StudentRequest
 * @see StudentResponse
 */
@Service
public class StudentService {

    /**
     * Thread-safe in-memory storage for student records.
     * <p>
     * Maps student ID to Student record. Using ConcurrentHashMap ensures
     * thread-safety for concurrent access in a multi-threaded environment.
     * </p>
     */
    private final Map<String, Student> students = new ConcurrentHashMap<>();

    /**
     * Creates a new student and stores it in memory.
     * <p>
     * This method performs the following operations:
     * </p>
     * <ol>
     *   <li>Validates that the email is unique</li>
     *   <li>Creates a new Student record with generated ID and timestamp</li>
     *   <li>Stores the student in memory</li>
     *   <li>Returns the API response DTO</li>
     * </ol>
     *
     * @param request the student creation request containing name, email, and phone
     * @return StudentResponse containing the created student details including generated ID
     * @throws EmailAlreadyExistsException if a student with the same email already exists
     * @see Student#create(String, String, String)
     */
    public StudentResponse createStudent(StudentRequest request) {

        if (emailExists(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Create new student using factory method
        Student student = Student.create(request.getName(), request.getEmail(), request.getPhone());

        // Save to memory
        students.put(student.id(), student);

        // Return response using factory method
        return toStudentResponse(student);
    }

    /**
     * Converts a Student domain model to a StudentResponse API DTO.
     * <p>
     * This method handles the transformation between the internal domain model
     * (Student record) and the external API contract (StudentResponse).
     * </p>
     *
     * @param student the student domain model to convert
     * @return StudentResponse containing the student data for API responses
     */
    private StudentResponse toStudentResponse(Student student) {
        var response = new StudentResponse();
        response.setId(student.id());
        response.setName(student.name());
        response.setEmail(student.email());
        response.setPhone(student.phone());
        return response;
    }

    /**
     * Returns the total number of students currently stored.
     * <p>
     * This method is primarily used for testing and monitoring purposes.
     * </p>
     *
     * @return the count of students in storage
     */
    public int getStudentCount() {
        return students.size();
    }

    /**
     * Checks if a student with the given email already exists.
     * <p>
     * This method performs a stream-based search through all stored students
     * to enforce the unique email constraint. It uses case-sensitive comparison.
     * </p>
     *
     * @param email the email address to check
     * @return true if a student with this email exists, false otherwise
     */
    private boolean emailExists(String email) {
        return students
            .values()
            .stream()
            .anyMatch(student -> student.email().equals(email));
    }
}
