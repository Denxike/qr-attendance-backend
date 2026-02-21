package com.Qr.Qr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.Qr.Qr.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

    @Entity
    @Table(name = "course", indexes = {
            @Index(name = "idx_course_code", columnList = "course_code"),
            @Index(name = "idx_teacher", columnList = "teacher_id"),
            @Index(name = "idx_semester", columnList = "semester")
    })
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Course {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "course_code", nullable = false, unique = true, length = 20)
        private String courseCode; // CS401

        @Column(name = "course_name", nullable = false, length = 100)
        private String courseName;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(columnDefinition = "INT DEFAULT 3")
        private Integer credits = 3;

        @Column(length = 50)
        private String semester; // Fall 2024

        @ManyToOne
        @JoinColumn(name = "teacher_id", nullable = false)
        private Teacher teacher;

        @ManyToOne
        @JoinColumn(name = "department_id", nullable = false)
        private Department department;

        @Column(name = "is_active")
        private Boolean isActive = true;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;


        @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
        private List<StudentCourseEnrollment> enrollments = new ArrayList<>();

        @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
        private List<QrSession> qrSessions = new ArrayList<>();

        @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Attendance> attendances = new ArrayList<>();
    }


