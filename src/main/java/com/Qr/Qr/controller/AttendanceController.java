package com.Qr.Qr.controller;

import com.Qr.Qr.dto.response.AttendanceResponse;
import com.Qr.Qr.repository.AttendanceRepository;
import com.Qr.Qr.repository.QRSessionRepository;
import com.Qr.Qr.repository.StudentRepository;
import com.Qr.Qr.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final StudentRepository studentRepository;
    private final QRSessionRepository qrSessionRepository;
    private final AttendanceRepository attendanceRepository;

    // ==================================================================
    // STUDENT: MARK ATTENDANCE (Fixed to use correct token field)
    // ==================================================================
    @PostMapping("/mark")
    @Transactional
    public ResponseEntity<?> markAttendance(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("=== MARK ATTENDANCE ===");
            System.out.println("Payload: " + payload);

            if (!payload.containsKey("studentId") || !payload.containsKey("qrToken")) {
                throw new RuntimeException("Missing student ID or QR token");
            }

            Long userId = Long.valueOf(payload.get("studentId").toString());
            String qrToken = payload.get("qrToken").toString().trim();

            System.out.println("User ID: " + userId);
            System.out.println("QR Token: " + qrToken);

            // Find student by USER ID
            com.Qr.Qr.model.Student student = studentRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            System.out.println("Found student: " + student.getId());

            com.Qr.Qr.model.QRSession session = qrSessionRepository.findBySessionToken(qrToken)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired QR token"));

            System.out.println("Found session: " + session.getId());

            // Check if already marked
            boolean alreadyMarked = attendanceRepository.existsByStudentIdAndQrSessionId(
                    student.getId(),
                    session.getId()
            );

            if (alreadyMarked) {
                throw new RuntimeException("You have already marked attendance for this session");
            }

            // Check expiry
            if (session.getExpiryTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("This QR code has expired");
            }

            if (!session.getIsActive()) {
                throw new RuntimeException("This session is no longer active");
            }

            // Create attendance record
            com.Qr.Qr.model.Attendance attendance = new com.Qr.Qr.model.Attendance();
            attendance.setStudent(student);
            attendance.setQrSession(session);
            attendance.setStatus("PRESENT");
            attendance.setMarkedAt(LocalDateTime.now());

            attendanceRepository.save(attendance);

            System.out.println("Attendance saved successfully!");

            return ResponseEntity.ok(Map.of(
                    "message", "Attendance marked successfully! ✅",
                    "sessionName", session.getSessionName(),
                    "courseName", session.getCourse().getCourseName()
            ));

        } catch (Exception e) {
            System.err.println("Error marking attendance: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(400)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ==================================================================
    // TEACHER: Get session attendance list (Live polling)
    // ==================================================================
    @GetMapping("/session/{sessionId}")
    @Transactional
    public ResponseEntity<?> getSessionAttendance(@PathVariable Long sessionId) {
        try {
            System.out.println("=== GET SESSION ATTENDANCE ===");
            System.out.println("Session ID: " + sessionId);

            List<com.Qr.Qr.model.Attendance> records = attendanceRepository.findByQrSessionId(sessionId);

            System.out.println("Found " + records.size() + " attendance records");

            List<Map<String, Object>> safeRecords = records.stream().map(att -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", att.getId());
                map.put("status", att.getStatus());
                map.put("markedAt", att.getMarkedAt());

                if (att.getStudent() != null && att.getStudent().getUser() != null) {
                    map.put("studentName", att.getStudent().getUser().getFullName());
                    map.put("studentId", att.getStudent().getStudentId());
                } else {
                    map.put("studentName", "Unknown Student");
                    map.put("studentId", "N/A");
                }

                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(safeRecords);

        } catch (Exception e) {
            System.err.println("Error fetching session attendance: " + e.getMessage());
            return ResponseEntity.status(500)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ==================================================================
    // TEACHER: Get live scan count
    // ==================================================================
    @GetMapping("/session/{sessionId}/count")
    public ResponseEntity<?> getSessionScanCount(@PathVariable Long sessionId) {
        try {
            long count = attendanceRepository.countByQrSessionId(sessionId);
            System.out.println("Session " + sessionId + " has " + count + " scans");
            return ResponseEntity.ok(Map.of("scans", count));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("scans", 0));
        }
    }

    // ==================================================================
    // STUDENT: Get attendance history (FIXED - Single endpoint only!)
    // ==================================================================
    @GetMapping("/student/{userId}/history")
    @Transactional
    public ResponseEntity<?> getStudentHistory(@PathVariable Long userId) {
        try {
            System.out.println("=== GET STUDENT HISTORY ===");
            System.out.println("User ID: " + userId);

            com.Qr.Qr.model.Student student = studentRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            System.out.println("Found student: " + student.getId());

            List<com.Qr.Qr.model.Attendance> attendances = attendanceRepository.findByStudentId(student.getId());

            System.out.println("Found " + attendances.size() + " attendance records");

            List<Map<String, Object>> safeHistory = attendances.stream()
                    .map(att -> {
                        Map<String, Object> record = new HashMap<>();
                        record.put("id", att.getId());
                        record.put("status", att.getStatus());
                        record.put("markedAt", att.getMarkedAt());

                        if (att.getQrSession() != null) {
                            record.put("sessionName", att.getQrSession().getSessionName());

                            if (att.getQrSession().getCourse() != null) {
                                record.put("courseName", att.getQrSession().getCourse().getCourseName());
                                record.put("courseCode", att.getQrSession().getCourse().getCourseCode());
                            } else {
                                record.put("courseName", "Unknown Course");
                                record.put("courseCode", "N/A");
                            }
                        } else {
                            record.put("sessionName", "Standard Session");
                            record.put("courseName", "Unknown Course");
                            record.put("courseCode", "N/A");
                        }

                        return record;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(safeHistory);

        } catch (Exception e) {
            System.err.println("Error fetching student history: " + e.getMessage());
            return ResponseEntity.ok(List.of()); // Return empty list instead of error
        }
    }

    // ==================================================================
    // OTHER ENDPOINTS (Keep for compatibility)
    // ==================================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<AttendanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<List<AttendanceResponse>> getByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                attendanceService.getAttendanceByStudentAndCourse(studentId, courseId));
    }
    // ==================================================================
// TEACHER: Get Course Attendance Report (Matrix Data)
// ==================================================================
    @GetMapping("/course/{courseId}/report")
    @Transactional
    public ResponseEntity<?> getCourseAttendanceReport(@PathVariable Long courseId) {
        try {
            System.out.println("=== GENERATE COURSE REPORT ===");
            System.out.println("Course ID: " + courseId);

            // Get all attendance records for this course
            List<com.Qr.Qr.model.Attendance> attendances = attendanceRepository
                    .findByQrSessionCourseIdOrderByMarkedAtDesc(courseId);

            System.out.println("Found " + attendances.size() + " attendance records");

            // Map to safe structure for frontend
            List<Map<String, Object>> reportData = attendances.stream()
                    .map(att -> {
                        Map<String, Object> record = new HashMap<>();

                        // Student info
                        if (att.getStudent() != null && att.getStudent().getUser() != null) {
                            record.put("studentId", att.getStudent().getStudentId());
                            record.put("studentName", att.getStudent().getUser().getFullName());
                        } else {
                            record.put("studentId", "N/A");
                            record.put("studentName", "Unknown Student");
                        }

                        // Session info
                        if (att.getQrSession() != null) {
                            record.put("sessionName", att.getQrSession().getSessionName());
                            record.put("sessionId", att.getQrSession().getId());
                        } else {
                            record.put("sessionName", "Unknown Session");
                            record.put("sessionId", null);
                        }

                        // Attendance details
                        record.put("status", att.getStatus());
                        record.put("markedAt", att.getMarkedAt());

                        return record;
                    })
                    .collect(Collectors.toList());

            System.out.println("Report generated successfully");

            return ResponseEntity.ok(reportData);

        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Failed to generate report: " + e.getMessage()));
        }
    }

}