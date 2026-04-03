package com.Qr.Qr.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.Qr.Qr.dto.request.LoginRequest;
import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.response.LoginResponse;
import com.Qr.Qr.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hash-password")
public ResponseEntity<String> hashPassword(@RequestParam String password) {
    return ResponseEntity.ok(passwordEncoder.encode(password));
}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody StudentRegistrationRequest request) {
    try {
        // Register the user
        authService.register(request);
        
        // Auto-login after registration
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword(request.getPassword());
        
        String token = authService.login(loginRequest);
        
        // Return token with user info
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Registration successful");
        
        return ResponseEntity.ok(response);
        
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(Map.of("message", e.getMessage()));
    }
}
}
