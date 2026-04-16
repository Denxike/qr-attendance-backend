package com.Qr.Qr.controller;

import com.Qr.Qr.model.User;
import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.dto.request.LoginRequest;
import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.request.TeacherRegistrationRequest;
import com.Qr.Qr.dto.response.LoginResponse;
import com.Qr.Qr.service.AuthService;
import com.Qr.Qr.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    try {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    } catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Invalid email or password"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", "Login failed: " + e.getMessage()));
    }
}
 
   @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        try {
            authService.registerStudent(request);
            
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(request.getEmail());
            loginRequest.setPassword(request.getPassword());
            
            LoginResponse loginResponse = authService.login(loginRequest);
            
            return ResponseEntity.ok(loginResponse);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        try {
            authService.registerTeacher(request);
            
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(request.getEmail());
            loginRequest.setPassword(request.getPassword());
            
            LoginResponse loginResponse = authService.login(loginRequest);
            
            return ResponseEntity.ok(loginResponse);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/hash-password")
    public ResponseEntity<Map<String, String>> hashPassword(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("hash", hash);
        return ResponseEntity.ok(response);
    }
      @PostMapping("/forgot-password")
public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    
    try {
        // Check if user exists
        userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("No user found with this email"));
        
        // TODO: Implement actual email sending logic
        // For now, just return success
        
        return ResponseEntity.ok(Map.of(
            "message", "Password reset link sent to your email"
        ));
        
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(Map.of("message", e.getMessage()));
    }
}
   }
