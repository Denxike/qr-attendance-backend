package com.Qr.Qr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.Qr.Qr.dto.request.LoginRequest;
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
}
