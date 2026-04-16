package com.Qr.Qr.controller;

import com.Qr.Qr.dto.request.QrGenerationRequest;
import com.Qr.Qr.model.*;
import com.Qr.Qr.repository.*;
import com.Qr.Qr.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TeacherController {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final QRSessionRepository qrSessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final QrCodeService qrCodeService;

    // Get current teacher
    private Teacher getCurrentTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        System.out.println("Getting teacher for email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Found user: " + user.getId() + " - " + user.getFullName());

        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));

        System.out.println("Found teacher: " + teacher.getId());

        return teacher;
    }

    // Get teacher's courses
    @GetMapping("/courses")
    public ResponseEntity<?> getTeacherCourses() {
        try {
            System.out.println("=== GET TEACHER COURSES ===");

            Teacher teacher = getCurrentTeacher();

            System.out.println("Fetching courses for teacher ID: " + teacher.getId());

            List<Course> courses = courseRepository.findByTeacherId(teacher.getId());

            System.out.println("Found " + courses.size() + " courses");

            if (courses.isEmpty()) {
                System.out.println("WARNING: No courses found for teacher " + teacher.getId());
            }

            return ResponseEntity.ok(courses);

        } catch (Exception e) {
            System.err.println("Error fetching courses: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    // Generate QR Code
    @PostMapping("/generate-qr")
    public ResponseEntity<?> generateQR(@RequestBody QrGenerationRequest request) {
        try {
            Teacher teacher = getCurrentTeacher();

            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (!course.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN)
                        .body(java.util.Map.of("message", "You don't have permission for this course"));
            }

            // 👇 Here is the magic! Passing the 4 parameters from the request and the teacher
            QRSession session = qrCodeService.generateQRCode(
                    request.getCourseId(),
                    teacher.getId(),
                    request.getSessionName(),
                    request.getDurationMinutes() // Note: Use getDuration() if that's what your DTO has
            );

            return ResponseEntity.ok(session);

        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Error: " + e.getMessage()));
        }
    }
    // Get active sessions
    @GetMapping("/active-sessions")
    public ResponseEntity<?> getActiveSessions() {
        try {
            System.out.println("=== GET ACTIVE SESSIONS ===");

            Teacher teacher = getCurrentTeacher();

            List<QRSession> sessions = qrSessionRepository.findByTeacherIdAndIsActiveTrue(teacher.getId());

            System.out.println("Found " + sessions.size() + " active sessions");

            return ResponseEntity.ok(sessions);

        } catch (Exception e) {
            System.err.println("Error fetching active sessions: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    // Deactivate session
    @PostMapping("/deactivate-session/{sessionId}")
    public ResponseEntity<?> deactivateSession(@PathVariable Long sessionId) {
        try {
            System.out.println("=== DEACTIVATE SESSION ===");
            System.out.println("Session ID: " + sessionId);

            Teacher teacher = getCurrentTeacher();

            QRSession session = qrSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            // Verify ownership
            if (!session.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Not authorized"));
            }

            session.setIsActive(false);
            qrSessionRepository.save(session);

            System.out.println("Session deactivated successfully");

            return ResponseEntity.ok(Map.of("message", "Session deactivated"));

        } catch (Exception e) {
            System.err.println("Error deactivating session: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    // Get session attendance
    @GetMapping("/session/{sessionId}/attendance")
    public ResponseEntity<?> getSessionAttendance(@PathVariable Long sessionId) {
        try {
            Teacher teacher = getCurrentTeacher();

            QRSession session = qrSessionRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));

            if (!session.getTeacher().getId().equals(teacher.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Not authorized"));
            }

            List<Attendance> attendanceList = attendanceRepository.findByQrSessionId(sessionId);

            return ResponseEntity.ok(attendanceList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    // Dashboard stats
    @GetMapping("/dashboard-stats")
    public ResponseEntity<?> getDashboardStats() {
        try {
            Teacher teacher = getCurrentTeacher();

            List<Course> courses = courseRepository.findByTeacherId(teacher.getId());
            List<QRSession> allSessions = qrSessionRepository.findByTeacherId(teacher.getId());
            List<QRSession> activeSessions = qrSessionRepository.findByTeacherIdAndIsActiveTrue(teacher.getId());

            long totalAttendance = attendanceRepository.countByQrSessionTeacherId(teacher.getId());

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCourses", courses.size());
            stats.put("totalSessions", allSessions.size());
            stats.put("activeSessions", activeSessions.size());
            stats.put("totalAttendance", totalAttendance);

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}