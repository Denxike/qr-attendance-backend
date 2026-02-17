package com.Qr.Qr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "qr_session", indexes = {
        @Index(name = "idx_session_token", columnList = "session_token"),
        @Index(name = "idx_course", columnList = "course_id"),
        @Index(name = "idx_expiry", columnList = "expiry_time")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_token", nullable = false, unique = true)
    private String sessionToken; // UUID

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "session_name", length = 100)
    private String sessionName; // "Week 5 Lecture"

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "total_scans")
    private Integer totalScans = 0;

    // Relationships
    @OneToMany(mappedBy = "qrSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();
}
