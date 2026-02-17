package com.Qr.Qr.repository;

import com.Qr.Qr.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByTeacher_IdAndIsActive(Long teacherId, Boolean isActive);

    @Query("SELECT c FROM Course c " +
            "JOIN StudentCourseEnrollment e ON e.course.id = c.id " +
            "WHERE e.student.id = :studentId " +
            "AND e.status = 'ENROLLED'")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

    Boolean existsByCourseCode(String courseCode);
    Long countByDepartmentId(Long departmentId);
    Long countByTeacherId(Long teacherId);

    // All active courses not yet enrolled by student
    @Query("SELECT c FROM Course c WHERE c.isActive = true AND c.id NOT IN " +
            "(SELECT e.course.id FROM StudentCourseEnrollment e " +
            "WHERE e.student.id = :studentId AND e.status = 'ENROLLED')")
    List<Course> findAvailableCoursesForStudent(@Param("studentId") Long studentId);
}
