package com.Qr.Qr.controller;

import com.Qr.Qr.model.Course;
import com.Qr.Qr.model.Teacher;
import com.Qr.Qr.model.User;
import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.repository.CourseRepository;
import com.Qr.Qr.repository.DepartmentRepository;
import com.Qr.Qr.repository.TeacherRepository;
import com.Qr.Qr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;

    // --- COURSES ---

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.ok(savedCourse);
    }

    // --- TEACHERS ---

    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherRepository.findAll());
    }

    @PostMapping("/teachers")
    public ResponseEntity<?> createTeacher(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));
        }

        // 1. Create the base User account
        User newUser = new User();
        newUser.setFullName(request.get("fullName"));
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(request.get("password")));
        newUser.setRole(Role.TEACHER); // Force TEACHER role
        newUser.setIsActive(true);
        User savedUser = userRepository.save(newUser);

        // 2. Create the Teacher profile linked to the User
        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setEmployeeId(request.get("employeeId"));
        teacher.setPhoneNumber(request.get("phoneNumber"));
        
        // Fetch and set department
        Long deptId = Long.parseLong(request.get("departmentId"));
        departmentRepository.findById(deptId).ifPresent(teacher::setDepartment);

        Teacher savedTeacher = teacherRepository.save(teacher);

        return ResponseEntity.ok(Map.of("message", "Teacher created successfully"));
    }
}
