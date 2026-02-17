package com.Qr.Qr.model;

import com.Qr.Qr.model.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_course_enrollment",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_enrollment",
                columnNames = {"student_id", "course_id"}
        ),
        indexes = {
                @Index(name = "idx_student", columnList = "student_id"),
                @Index(name = "idx_course", columnList = "course_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @CreationTimestamp
    @Column(name = "enrollment_date", updatable = false)
    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ENROLLED;
}
