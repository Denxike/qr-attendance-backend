package com.Qr.Qr.controller;

import com.Qr.Qr.model.Department;
import com.Qr.Qr.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
 // Allows React to fetch data
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    // This is the endpoint your React app is calling!
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }
}
