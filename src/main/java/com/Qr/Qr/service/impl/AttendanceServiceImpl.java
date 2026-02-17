package com.Qr.Qr.service.impl;

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
import com.Qr.Qr.repository.QrSessionRepository;
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
    private final QrSessionRepository qrSessionRepository;
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

        List<Attendance> attendances = attendanceRepository
                .findByStudentIdAndCourseId(studentId, courseId);

        return attendanceMapper.toResponseList(attendances);
    }
    @Override
    public AttendanceResponse markAttendance(MarkAttendanceRequest request) {
        QrSession session = qrSessionRepository.findBySessionToken(request.getQrToken())
                .orElseThrow(()-> new InvalidQRCodeException("Invalid QR code"));

        if (!session.getIsActive()){
            throw new InvalidQRCodeException("Qr code is no longer active");
        }
        if (session.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new InvalidQRCodeException("QR code has expired");
        }

        Student student = studentRepository.findByStudentId(request.getStudentId())
                .orElseThrow(()-> new ResourceNotFoundException("Student","id",request.getStudentId()));
        if (!enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                student.getId(),
                session.getCourse().getId(),
                EnrollmentStatus.ENROLLED
        )){
            throw new UnauthorizedException("you are not enrolled in this course");
        }
        if (attendanceRepository.existsByStudentIdAndQrSessionId(student.getId(), session.getId())){
            throw new DuplicateAttendanceException("You have already marked attendance for this session");
        }

        AttendanceStatus status = AttendanceStatus.PRESENT;
        LocalDateTime now = LocalDateTime.now();

        if(now.isAfter(session.getStartTime().plusMinutes(10))) {
            status = AttendanceStatus.LATE;
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(session.getCourse());
        attendance.setQrSession(session);
        attendance.setStatus(status);

        Attendance savedAttendance = attendanceRepository.save(attendance);

        session.setTotalScans(session.getTotalScans() + 1);
        qrSessionRepository.save(session);

        return attendanceMapper.toResponse(savedAttendance);
    }

    @Override
    public AttendanceResponse getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));

        return attendanceMapper.toResponse(attendance);
    }
}
