package com.Qr.Qr.service;

import com.Qr.Qr.dto.request.TeacherRegistrationRequest;
import com.Qr.Qr.dto.response.TeacherResponse;

public interface TeacherService {
    TeacherResponse registerTeacher(TeacherRegistrationRequest request);
}
