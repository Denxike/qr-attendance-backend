package com.Qr.Qr.service.impl;

import com.Qr.Qr.dto.request.QrGenerationRequest;
import com.Qr.Qr.dto.response.QrGenerationResponse;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.exception.UnauthorizedException;
import com.Qr.Qr.model.Course;
import com.Qr.Qr.model.QrSession;
import com.Qr.Qr.model.Teacher;
import com.Qr.Qr.repository.AttendanceRepository;
import com.Qr.Qr.repository.CourseRepository;
import com.Qr.Qr.repository.QrSessionRepository;
import com.Qr.Qr.repository.TeacherRepository;
import com.Qr.Qr.service.QrCodeService;
import com.Qr.Qr.util.QRCodeUtil;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
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
    private final QrSessionRepository qrSessionRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;
    private final QRCodeUtil qrCodeUtil;

    @Override
    public QrGenerationResponse generateQRCode(QrGenerationRequest request, Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher","id",teacherId));
        log.info("Authenticated teacherId = {}", teacherId);


        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course","Id",request.getCourseId()));
        log.info("Course.teacherId = {}", course.getTeacher().getId());
        if(!course.getTeacher().getId().equals(teacherId)){
           throw new UnauthorizedException("You are not authorized to generateQR for this course");
        }
        qrSessionRepository.findByCourseIdAndIsActiveTrue(course.getId())
                .ifPresent(existingSession -> {
                        existingSession.setIsActive(false);
                        qrSessionRepository.save(existingSession);
                });
        String token = UUID.randomUUID().toString();
        while(qrSessionRepository.existsBySessionToken(token)) {
            token = UUID.randomUUID().toString();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(request.getDurationMinutes());

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

        String qrCodeImage;
        try{
            qrCodeImage = qrCodeUtil.generateQRCodeImage(token);
        }catch (WriterException | IOException e){
            throw new RuntimeException("Failed to generate QR code image: "+e.getMessage());
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
        .orElseThrow(()->new ResourceNotFoundException("Qr Session","id",sessionId));

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
    public void deactivateSession(Long sessionId, Long teacherId) {
        QrSession session = qrSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Qr Session","id",sessionId));
        if (!session.getTeacher().getId().equals(teacherId)){
            throw new UnauthorizedException("You are not authorized to deactivate this session");
        }
        session.setIsActive(false);
        qrSessionRepository.save(session);

    }
}
