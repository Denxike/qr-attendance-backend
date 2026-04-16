package com.Qr.Qr.repository;

import com.Qr.Qr.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Valid methods based on Attendance entity structure
    List<Attendance> findByStudentId(Long studentId);
    long countByQrSessionTeacherId(Long teacherId);
    List<Attendance> findByQrSessionId(Long sessionId);
    boolean existsByStudentIdAndQrSessionId(Long studentId, Long qrSessionId);
    long countByQrSessionId(Long qrSessionId);
    List<Attendance> findByStudentIdAndQrSessionCourseId(Long studentId, Long courseId);
    List<Attendance> findByQrSessionCourseIdOrderByMarkedAtDesc(Long courseId);
}
