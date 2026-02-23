package com.Qr.Qr.repository;

import com.Qr.Qr.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Spring Data naming conventions
    List<Attendance> findByStudent_Id(Long studentId);
    List<Attendance> findByQrSession_Id(Long sessionId);
    boolean existsByStudent_IdAndQrSession_Id(Long studentId, Long qrSessionId);
    
    // Alternative naming (used in some service methods)
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByQrSessionId(Long sessionId);
    List<Attendance> findByQRSessionId(Long sessionId);
    List<Attendance> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
