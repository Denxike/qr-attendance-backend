package com.Qr.Qr.service;

import com.Qr.Qr.dto.request.MarkAttendanceRequest;
import com.Qr.Qr.dto.response.AttendanceResponse;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse markAttendance(MarkAttendanceRequest request);
    AttendanceResponse getAttendanceById(Long id);


    List<AttendanceResponse> getAttendanceBySession(Long sessionId);

    List<AttendanceResponse> getAttendanceByStudent(Long studentId);

    List<AttendanceResponse> getAttendanceByStudentAndCourse(Long studentId, Long courseId);

}
