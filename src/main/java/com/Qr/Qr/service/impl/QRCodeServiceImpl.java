package com.Qr.Qr.service.impl;

import com.Qr.Qr.model.QRSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import com.Qr.Qr.dto.response.QrGenerationResponse;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.exception.UnauthorizedException;
import com.Qr.Qr.model.Course;
import com.Qr.Qr.model.Teacher;
import com.Qr.Qr.repository.AttendanceRepository;
import com.Qr.Qr.repository.CourseRepository;
import com.Qr.Qr.repository.QRSessionRepository;
import com.Qr.Qr.repository.TeacherRepository;
import com.Qr.Qr.service.QrCodeService;
import com.Qr.Qr.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QRCodeServiceImpl implements QrCodeService {
    private final QRSessionRepository qrSessionRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;
    private final QRCodeUtil qrCodeUtil;

    @Override
    @Transactional
    public QRSession generateQRCode(Long courseId, Long teacherId, String sessionName, Integer duration) {

        // 1. Find Course and Teacher
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // 2. Deactivate old sessions for this course
        qrSessionRepository.findByCourseIdAndIsActiveTrue(courseId)
                .ifPresent(existingSession -> {
                    existingSession.setIsActive(false);
                    qrSessionRepository.save(existingSession);
                });

        // 3. Generate unique Token
        String token = java.util.UUID.randomUUID().toString();
        // Note: Change 'existsBySessionToken' to 'existsByToken' if your repository uses that name
        while (qrSessionRepository.existsBySessionToken(token)) {
            token = java.util.UUID.randomUUID().toString();
        }

        // 4. Calculate Time
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime expiryTime = now.plusMinutes(duration);

        // 5. Save new Session
        QRSession session = new QRSession();
        session.setSessionToken(token);
        session.setCourse(course);
        session.setTeacher(teacher);
        session.setSessionName(sessionName);
        session.setStartTime(now);
        session.setExpiryTime(expiryTime);
        session.setIsActive(true);
        session.setTotalScans(0);

        // Save it to DB
        QRSession savedSession = qrSessionRepository.save(session);

        // 6. Generate the Image and attach it to the Transient field
        try {
            // Using your local frontend URL for testing
            String qrData = "http://10.192.38.27:3000/mark-attendance?token=" + token;
            String qrCodeImage = qrCodeUtil.generateQRCodeImage(qrData);

            // Because of @Transient, this goes to React but NOT the database
            savedSession.setQrCodeImage(qrCodeImage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code image: " + e.getMessage());
        }

        return savedSession; // Sends the object + the transient image to the controller
    }
    @Override
    public QrGenerationResponse getSessionDetails(Long sessionId) {
        QRSession session = qrSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Qr Session", "id", sessionId));

        int scanCount = attendanceRepository.findByQrSessionId(sessionId).size();

        session.setTotalScans(scanCount);
        qrSessionRepository.save(session);

        return QrGenerationResponse.builder()
                .sessionId(session.getId())
                .sessionToken(session.getSessionToken())
                .courseId(session.getCourse().getId())
                .courseCode(session.getCourse().getCourseCode())
                .courseName(session.getCourse().getCourseName())
                .sessionName(session.getSessionName())
                .startTime(session.getStartTime())
                .expiryTime(session.getExpiryTime())
                .isActive(session.getIsActive())
                .totalScans(scanCount)
                .build();
    }

    @Override
    public void deactivateSession(Long sessionId, Long dummyTeacherId) {
        // 1. SECURE AUTH: Get email from the JWT token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // 2. Find Teacher by their email
        Teacher teacher = teacherRepository.findByUserEmail(email)
                .orElseThrow(() -> new UnauthorizedException("No teacher profile found"));

        QRSession session = qrSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Qr Session", "id", sessionId));
        
        // 3. Verify Ownership securely
        if (!session.getTeacher().getId().equals(teacher.getId())) {
            throw new UnauthorizedException("You are not authorized to deactivate this session");
        }
        
        session.setIsActive(false);
        qrSessionRepository.save(session);
    }
    @Override
    public boolean validateQRToken(String token) {
        // Look up the session by its token string
        // Note: Change 'findBySessionToken' to 'findByToken' if that's what your repository uses!
        java.util.Optional<QRSession> sessionOpt = qrSessionRepository.findBySessionToken(token);

        if (sessionOpt.isPresent()) {
            QRSession session = sessionOpt.get();
            // It is only valid if it is marked active AND the current time is before the expiry time
            return session.getIsActive() && session.getExpiryTime().isAfter(java.time.LocalDateTime.now());
        }
        return false;
    }

    @Override
    public QRSession getSessionByToken(String token) {
        return qrSessionRepository.findBySessionToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired QR Token"));
    }
}
