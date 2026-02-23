package com.Qr.Qr.repository;

import com.Qr.Qr.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Valid methods based on Attendance entity structure
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByQrSessionId(Long sessionId);
    boolean existsByStudent_IdAndQrSession_Id(Long studentId, Long qrSessionId);
}
