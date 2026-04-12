package com.Qr.Qr.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import com.Qr.Qr.dto.request.QrGenerationRequest;
import com.Qr.Qr.dto.response.QrGenerationResponse;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.exception.UnauthorizedException;
import com.Qr.Qr.model.Course;
import com.Qr.Qr.model.QrSession;
import com.Qr.Qr.model.Teacher;
import com.Qr.Qr.repository.AttendanceRepository;
import com.Qr.Qr.repository.CourseRepository;
import com.Qr.Qr.repository.QRSessionRepository;
import com.Qr.Qr.repository.TeacherRepository;
import com.Qr.Qr.service.QrCodeService;
import com.Qr.Qr.util.QRCodeUtil;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

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
    public QrGenerationResponse generateQRCode(QrGenerationRequest request, Long dummyTeacherId) {
        
        // 1. SECURE AUTH: Get email from the JWT token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authenticated email = {}", email);

        // 2. Find Teacher by their email (Ignore the request ID)
        Teacher teacher = teacherRepository.findByUserEmail(email)
                .orElseThrow(() -> new UnauthorizedException("No teacher profile found for this email"));

        // 3. Find the Course
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", request.getCourseId()));
        
        // 4. Verify Ownership
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new UnauthorizedException("You are not authorized to generate QR for this course");
        }

        // 5. Deactivate previous sessions
        qrSessionRepository.findByCourseIdAndIsActiveTrue(course.getId())
                .ifPresent(existingSession -> {
                    existingSession.setIsActive(false);
                    qrSessionRepository.save(existingSession);
                });

        // 6. Generate Token
        String token = UUID.randomUUID().toString();
        while (qrSessionRepository.existsBySessionToken(token)) {
            token = UUID.randomUUID().toString();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(request.getDurationMinutes());

        // 7. Save new Session
        QrSession session = new QrSession();
        session.setSessionToken(token);
        session.setCourse(course);
        session.setTeacher(teacher);
        session.setSessionName(request.getSessionName());
        session.setStartTime(now);
        session.setExpiryTime(expiryTime);
        session.setIsActive(true);
        session.setTotalScans(0);

        QrSession savedSession = qrSessionRepository.save(session);

        // 8. Generate QR Image using Vercel URL
        String qrCodeImage;
        try {
            String qrData = "https://qr-attendance-frontend-two.vercel.app/mark-attendance?token=" + token;
            qrCodeImage = qrCodeUtil.generateQRCodeImage(qrData);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code image: " + e.getMessage());
        }

        return QrGenerationResponse.builder()
                .sessionId(savedSession.getId())
                .sessionToken(token)
                .qrCodeImage(qrCodeImage)
                .courseId(course.getId())
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .sessionName(request.getSessionName())
                .startTime(now)
                .expiryTime(expiryTime)
                .durationMinutes(request.getDurationMinutes())
                .isActive(true)
                .totalScans(0)
                .build();
    }

    @Override
    public QrGenerationResponse getSessionDetails(Long sessionId) {
        QrSession session = qrSessionRepository.findById(sessionId)
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

        QrSession session = qrSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Qr Session", "id", sessionId));
        
        // 3. Verify Ownership securely
        if (!session.getTeacher().getId().equals(teacher.getId())) {
            throw new UnauthorizedException("You are not authorized to deactivate this session");
        }
        
        session.setIsActive(false);
        qrSessionRepository.save(session);
    }
}
