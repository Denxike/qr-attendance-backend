package com.Qr.Qr.repository;
import com.Qr.Qr.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Boolean existsByStudentIdAndQrSessionId(Long studentId, Long sessionId);

    List<Attendance> findByStudentId(Long studentId);

    List<Attendance> findByQrSessionId(Long sessionId);
    List<Attendance> findByStudentIdAndCourseId(Long studentId, Long courseId);

}
