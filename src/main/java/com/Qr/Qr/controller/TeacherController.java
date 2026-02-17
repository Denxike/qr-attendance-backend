package com.Qr.Qr.controller;

import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.request.TeacherRegistrationRequest;
import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.dto.response.StudentResponse;
import com.Qr.Qr.dto.response.TeacherResponse;
import com.Qr.Qr.service.CourseService;
import com.Qr.Qr.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    private final CourseService courseService;

    @PostMapping("/register")
    public ResponseEntity<TeacherResponse> registerStudent(
            @Valid @RequestBody TeacherRegistrationRequest request
    ){
        TeacherResponse response = teacherService.registerTeacher(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{teacherId}/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<CourseResponse>> getTeacherCourses(@PathVariable Long teacherId) {
        List<CourseResponse> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }
}
