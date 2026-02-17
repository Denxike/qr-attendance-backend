package com.Qr.Qr.service;

import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse registerStudent(StudentRegistrationRequest request);

}
