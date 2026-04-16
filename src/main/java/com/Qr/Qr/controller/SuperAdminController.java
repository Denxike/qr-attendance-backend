package com.Qr.Qr.controller;

import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.model.Department;
import com.Qr.Qr.model.User;
import com.Qr.Qr.repository.DepartmentRepository;
import com.Qr.Qr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. Create a new Department
    @PostMapping("/departments")
    public ResponseEntity<?> createDepartment(@RequestBody Department department) {
        if (department.getDepartmentName() == null || department.getDepartmentName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Department name is required"));
        }
        
        Department savedDepartment = departmentRepository.save(department);
        return ResponseEntity.ok(savedDepartment);
    }

    // 2. Create a new Admin (Chairman of Department)
    @PostMapping("/admins")
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));
        }

        User newAdmin = new User();
        newAdmin.setFullName(request.get("fullName"));
        newAdmin.setEmail(email);
        newAdmin.setPassword(passwordEncoder.encode(request.get("password"))); // Hash the password!
        newAdmin.setRole(Role.ADMIN);
        newAdmin.setIsActive(true);

        userRepository.save(newAdmin);

        return ResponseEntity.ok(Map.of("message", "Admin created successfully"));
    }
}
