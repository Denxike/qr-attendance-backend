package com.Qr.Qr.service.impl;

import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.dto.request.LoginRequest;
import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.request.TeacherRegistrationRequest;
import com.Qr.Qr.dto.response.LoginResponse;
import com.Qr.Qr.model.*;
import com.Qr.Qr.repository.*;
import com.Qr.Qr.security.JwtUtils;
import com.Qr.Qr.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

       @Override
public LoginResponse login(LoginRequest request) {
    try {
        // First check if user exists
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));
        
        // Check if user is active
        if (!user.getIsActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }
        
        // Authenticate
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtils.generateToken(user.getEmail(),user.getRole().name());

        return LoginResponse.builder()
            .token(token)
            .email(user.getEmail())
            .fullName(user.getFullName())
            .role(user.getRole().name())
            .userId(user.getId())
            .build();
            
    } catch (BadCredentialsException e) {
        throw new IllegalArgumentException("Invalid email or password");
    }
}
    @Override
    @Transactional
    public void registerStudent(StudentRegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.STUDENT);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        user = userRepository.save(user);

        Student student = new Student();
        student.setUser(user);
        student.setStudentId(request.getStudentId());
        student.setDepartment(department);
        student.setYearOfStudy(request.getYearOfStudy());
        student.setPhoneNumber(request.getPhoneNumber());
        
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void registerTeacher(TeacherRegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.TEACHER);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setEmployeeId(request.getEmployeeId());
        teacher.setDepartment(department);
        teacher.setPhoneNumber(request.getPhoneNumber());
        
        teacherRepository.save(teacher);
    }
}
