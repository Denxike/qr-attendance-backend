package com.Qr.Qr.controller;

import com.Qr.Qr.model.Course;
import com.Qr.Qr.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses") // 👈 Notice: NO "/admin" in the URL!
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()") // 👈 Any logged-in user (Admin, Teacher, Student) can read this
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }
}
