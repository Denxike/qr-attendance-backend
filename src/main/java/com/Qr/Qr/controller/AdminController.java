package com.Qr.Qr.controller;

import com.Qr.Qr.dto.request.CreateCourseRequest;
import com.Qr.Qr.dto.request.CreateDepartmentRequest;
import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.dto.response.DepartmentResponse;
import com.Qr.Qr.service.AdminService;
import com.Qr.Qr.dto.request.EnrollStudentRequest;
import com.Qr.Qr.dto.response.StudentResponse;
import com.Qr.Qr.dto.response.TeacherResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;


    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(adminService.getAllDepartments());
    }

    @PostMapping("/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.createDepartment(request));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        adminService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(adminService.getAllCourses());
    }

    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.createCourse(request));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        adminService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Students ----
    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    // ---- Teachers ----
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherResponse>> getAllTeachers() {
        return ResponseEntity.ok(adminService.getAllTeachers());
    }

    // ---- Enrollment ----
    @PostMapping("/enroll")
    public ResponseEntity<Void> enrollStudent(
            @Valid @RequestBody EnrollStudentRequest request) {
        adminService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/enroll/{studentId}/{courseId}")
    public ResponseEntity<Void> unenrollStudent(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        adminService.unenrollStudent(studentId, courseId);
        return ResponseEntity.noContent().build();
    }
}
