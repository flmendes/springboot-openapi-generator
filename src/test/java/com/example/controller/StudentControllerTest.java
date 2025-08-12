package com.example.controller;

import com.example.exception.EmailAlreadyExistsException;
import com.example.service.StudentService;
import com.example.students.model.StudentRequest;
import com.example.students.model.StudentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createStudent_ShouldReturnCreated_WhenValidRequest() throws Exception {
        // Given
        StudentRequest request = new StudentRequest("John Doe", "john.doe@email.com", "(11) 99999-9999");
        
        StudentResponse response = new StudentResponse();
        response.setId("123e4567-e89b-12d3-a456-426614174000");
        response.setName("John Doe");
        response.setEmail("john.doe@email.com");
        response.setPhone("(11) 99999-9999");
        
        when(studentService.createStudent(any(StudentRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@email.com"))
                .andExpect(jsonPath("$.phone").value("(11) 99999-9999"));
    }

    @Test
    void createStudent_ShouldReturnBadRequest_WhenNameIsMissing() throws Exception {
        // Given
        StudentRequest request = new StudentRequest();
        request.setEmail("john.doe@email.com");
        request.setPhone("(11) 99999-9999");

        // When & Then
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudent_ShouldReturnBadRequest_WhenEmailIsMissing() throws Exception {
        // Given
        StudentRequest request = new StudentRequest();
        request.setName("John Doe");
        request.setPhone("(11) 99999-9999");

        // When & Then
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudent_ShouldReturnBadRequest_WhenPhoneIsMissing() throws Exception {
        // Given
        StudentRequest request = new StudentRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@email.com");

        // When & Then
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudent_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
        // Given
        StudentRequest request = new StudentRequest("John Doe", "invalid-email", "(11) 99999-9999");

        // When & Then
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudent_ShouldReturnConflict_WhenEmailAlreadyExists() throws Exception {
        // Given
        StudentRequest request = new StudentRequest("John Doe", "john.doe@email.com", "(11) 99999-9999");
        
        when(studentService.createStudent(any(StudentRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exists"));

        // When & Then
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Email already exists"))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void createStudent_ShouldReturnBadRequest_WhenRequestBodyIsEmpty() throws Exception {
        // When & Then
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudent_ShouldReturnInternalServerError_WhenRequestBodyIsInvalid() throws Exception {
        // When & Then - Invalid JSON returns 500 due to JSON parsing error
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}