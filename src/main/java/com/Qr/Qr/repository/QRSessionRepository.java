package com.Qr.Qr.repository;

import com.Qr.Qr.model.QrSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QRSessionRepository extends JpaRepository<QRSession, Long> {
    Optional<QrSession> findBySessionToken(String sessionToken);
    boolean existsBySessionToken(String sessionToken);
    List<QrSession> findByCourse_Id(Long courseId);
    Optional<QrSession> findByCourseIdAndIsActiveTrue(Long courseId);
}
