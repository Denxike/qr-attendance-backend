package com.Qr.Qr.service.impl;

import com.Qr.Qr.security.JwtUtils;
import com.Qr.Qr.dto.request.LoginRequest;
import com.Qr.Qr.dto.response.LoginResponse;
import com.Qr.Qr.exception.InvalidCredentialsException;
import com.Qr.Qr.model.User;
import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.repository.StudentRepository;
import com.Qr.Qr.repository.TeacherRepository;
import com.Qr.Qr.repository.UserRepository;
import com.Qr.Qr.security.JwtTokenProvider;
import com.Qr.Qr.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("User not found"));

	   String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());           

            // Set in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT


            // Get user


            // Build response
            LoginResponse.LoginResponseBuilder responseBuilder = LoginResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole().name());

            if (user.getRole() == Role.STUDENT) {
                studentRepository.findByUserId(user.getId())
                        .ifPresent(student -> responseBuilder.studentId(student.getId()));
            } else if (user.getRole() == Role.TEACHER) {
                teacherRepository.findByUserId(user.getId())
                        .ifPresent(teacher -> responseBuilder.teacherId(teacher.getId()));

            }
            return responseBuilder.build();
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }
}
