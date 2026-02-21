package com.Qr.Qr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "qr_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "session_token", unique = true, nullable = false)
    private String sessionToken;

    @Column(name = "session_name")
    private String sessionName;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "qrSession", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Attendance> attendances;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
