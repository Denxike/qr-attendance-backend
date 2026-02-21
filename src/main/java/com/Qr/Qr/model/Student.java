package com.Qr.Qr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name="student", indexes = {
        @Index(name = "idx_student_id", columnList = "student_id"),
        @Index(name = "idx_department", columnList = "department_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false,unique = true)
   @JsonIgnore
    private User user;

    @Column(name="student_id",nullable = false, unique = true)
    private String studentId;

    @ManyToOne
    @JoinColumn(name="department_id",nullable = false)
    private Department department;

    @Column(name="year_of_study")
    private Integer yearOfStudy;

    @Column(name= "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<StudentCourseEnrollment> enrollments;
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Attendance> attendances;
}
