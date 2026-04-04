package com.Qr.Qr.service;

import com.Qr.Qr.dto.request.LoginRequest;
import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.request.TeacherRegistrationRequest;
import com.Qr.Qr.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void registerStudent(StudentRegistrationRequest request);
    void registerTeacher(TeacherRegistrationRequest request);
}
