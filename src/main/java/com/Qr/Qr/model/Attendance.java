package com.Qr.Qr.model;

import com.Qr.Qr.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

    @Entity
    @Table(name = "attendance",
            uniqueConstraints = @UniqueConstraint(
                    name = "unique_attendance",
                    columnNames = {"student_id", "qr_session_id"}
            ),
            indexes = {
                    @Index(name = "idx_student_course", columnList = "student_id, course_id"),
                    @Index(name = "idx_session", columnList = "qr_session_id"),
                    @Index(name = "idx_marked_at", columnList = "marked_at")
            }
    )
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Attendance {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "student_id", nullable = false)
        private Student student;

        @ManyToOne
        @JoinColumn(name = "course_id", nullable = false)
        private Course course;

        @ManyToOne
        @JoinColumn(name = "qr_session_id", nullable = false)
        private QrSession qrSession;

        @CreationTimestamp
        @Column(name = "marked_at", updatable = false)
        private LocalDateTime markedAt;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private AttendanceStatus status = AttendanceStatus.PRESENT;

}
