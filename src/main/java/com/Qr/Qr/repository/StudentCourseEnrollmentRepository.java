package com.Qr.Qr.repository;

import com.Qr.Qr.model.Course;
import com.Qr.Qr.model.StudentCourseEnrollment;
import com.Qr.Qr.model.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseEnrollmentRepository extends JpaRepository<StudentCourseEnrollment, Long> {

    Boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, EnrollmentStatus status);

    Optional<StudentCourseEnrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    Long countByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    @Query("SELECT c FROM Course c WHERE c.id NOT IN " +
            "(SELECT e.course.id FROM StudentCourseEnrollment e " +
            "WHERE e.student.id = :studentId AND e.status = 'ENROLLED')" +
            "AND c.isActive = true")
    List<Course> findAvailableCoursesForStudent(@Param("studentId") Long studentId);
}
