package com.Qr.Qr.service.impl;

import java.util.stream.Collectors;
import com.Qr.Qr.dto.request.MarkAttendanceRequest;
import com.Qr.Qr.dto.response.AttendanceResponse;
import com.Qr.Qr.exception.DuplicateAttendanceException;
import com.Qr.Qr.exception.InvalidQRCodeException;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.exception.UnauthorizedException;
import com.Qr.Qr.mapper.AttendanceMapper;
import com.Qr.Qr.model.Attendance;
import com.Qr.Qr.model.QrSession;
import com.Qr.Qr.model.Student;
import com.Qr.Qr.model.enums.AttendanceStatus;
import com.Qr.Qr.model.enums.EnrollmentStatus;
import com.Qr.Qr.repository.AttendanceRepository;
import com.Qr.Qr.repository.QRSessionRepository;
import com.Qr.Qr.repository.StudentCourseEnrollmentRepository;
import com.Qr.Qr.repository.StudentRepository;
import com.Qr.Qr.service.AttendanceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final QRSessionRepository qrSessionRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseEnrollmentRepository enrollmentRepository;
    private final AttendanceMapper attendanceMapper;
    @Override
    public List<AttendanceResponse> getAttendanceBySession(Long sessionId) {
        log.info("Fetching attendance for session: {}", sessionId);

        List<Attendance> attendances = attendanceRepository
                .findByQrSessionId(sessionId);

        return attendanceMapper.toResponseList(attendances);
    }
    @Override
    public List<AttendanceResponse> getAttendanceByStudent(Long studentId) {
        log.info("Fetching attendance history for student: {}", studentId);

        List<Attendance> attendances = attendanceRepository
                .findByStudentId(studentId);

        return attendanceMapper.toResponseList(attendances);
    }

    @Override
    public List<AttendanceResponse> getAttendanceByStudentAndCourse(
            Long studentId, Long courseId) {

        log.info("Fetching attendance for student: {} in course: {}",
                studentId, courseId);

	List<Attendance> attendances = attendanceRepository.findByStudentId(studentId).stream()
    .filter(a -> a.getQrSession() != null && 
                 a.getQrSession().getCourse() != null && 
                 a.getQrSession().getCourse().getId().equals(courseId))
    .collect(Collectors.toList());

        return attendanceMapper.toResponseList(attendances);
    }

@Override
@Transactional
public AttendanceResponse markAttendance(MarkAttendanceRequest request) {
    Student student = studentRepository.findById(Long.parseLong(request.getStudentId()))
            .orElseThrow(() -> new RuntimeException("Student not found"));

    QrSession session = qrSessionRepository.findBySessionToken(request.getQrToken())
            .orElseThrow(() -> new RuntimeException("Invalid QR code"));

    if (!session.getIsActive() || LocalDateTime.now().isAfter(session.getExpiryTime())) {
        throw new RuntimeException("QR code has expired");
    }

    if (attendanceRepository.existsByStudent_IdAndQrSession_Id(student.getId(), session.getId())) {
        throw new RuntimeException("Attendance already marked for this session");
    }

    String status = "PRESENT";

    Attendance attendance = new Attendance();
    attendance.setStudent(student);
    attendance.setQrSession(session);
    attendance.setStatus(status);

    Attendance savedAttendance = attendanceRepository.save(attendance);

    return AttendanceMapper.toResponse(savedAttendance);
}
    @Override
    public AttendanceResponse getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));

        return attendanceMapper.toResponse(attendance);
    }
}
