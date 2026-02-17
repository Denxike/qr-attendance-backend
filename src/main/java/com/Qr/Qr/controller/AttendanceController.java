package com.Qr.Qr.controller;

import com.Qr.Qr.dto.request.MarkAttendanceRequest;
import com.Qr.Qr.dto.response.AttendanceResponse;
import com.Qr.Qr.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AttendanceResponse> markAttendance(
            @Valid @RequestBody MarkAttendanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.markAttendance(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<AttendanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<List<AttendanceResponse>> getBySession(
            @PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getAttendanceBySession(sessionId));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<List<AttendanceResponse>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<List<AttendanceResponse>> getByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                attendanceService.getAttendanceByStudentAndCourse(studentId, courseId));
    }
//    @GetMapping("/student/{studentId}")
//    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
//    public ResponseEntity<List<AttendanceResponse>> getStudentAttendance(@PathVariable Long studentId) {
//        List<AttendanceResponse> attendance = attendanceService.getAttendanceByStudent(studentId);
//        return ResponseEntity.ok(attendance);
//    }
}
