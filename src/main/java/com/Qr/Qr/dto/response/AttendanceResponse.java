package com.Qr.Qr.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceResponse {
    public Long id;
    private Long studentId;
    private String studentRegistrationId;
    private String studentName;

    // Course info
    private Long courseId;
    private String courseCode;
    private String courseName;

    // Session info
    private Long sessionId;
    private String sessionName;  // e.g., "Week 5 Lecture"

    // Attendance details
    private LocalDateTime markedAt;
    private String status;
}
