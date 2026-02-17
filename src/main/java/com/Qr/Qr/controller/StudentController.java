package com.Qr.Qr.controller;

import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.dto.response.StudentResponse;
import com.Qr.Qr.service.CourseService;
import com.Qr.Qr.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody StudentRegistrationRequest request
            ){
        StudentResponse response = studentService.registerStudent(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{studentId}/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponse>> getStudentCourses(@PathVariable Long studentId) {
        List<CourseResponse> courses = courseService.getCoursesByStudent(studentId);
        return ResponseEntity.ok(courses);
    }

}
