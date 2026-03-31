package com.Qr.Qr.controller;

import com.Qr.Qr.dto.response.*;
import com.Qr.Qr.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {
    
    private final SuperAdminService superAdminService;

    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(superAdminService.getDashboardStats());
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        return ResponseEntity.ok(superAdminService.getAllUsers());
    }

    @GetMapping("/system-reports")
    public ResponseEntity<Map<String, Object>> getSystemReports() {
        return ResponseEntity.ok(superAdminService.getSystemReports());
    }

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@RequestBody Map<String, String> request) {
        superAdminService.createAdmin(request);
        return ResponseEntity.ok("Admin created successfully");
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        superAdminService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
