package com.Qr.Qr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="department", indexes={
        @Index(name="idx_dept_id", columnList = "department_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="department_id",nullable = false,unique = true)
    private Long departmentId;

    @Column(name="department_name",nullable = false)
    private String departmentName;

    @ManyToOne
    @JoinColumn(name="head_of_dapartment")
    private Teacher headOfDepartment;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;
    private String description;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Teacher> teachers = new ArrayList<>();
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
}
