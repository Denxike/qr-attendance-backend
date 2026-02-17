package com.Qr.Qr.repository;
import com.Qr.Qr.model.QrSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrSessionRepository extends JpaRepository<QrSession,Long> {
    Optional<QrSession> findBySessionToken(String sessionToken);
    Optional<QrSession> findById(Long id);
    Optional<QrSession> findByCourseIdAndIsActiveTrue(Long courseId);
    Boolean existsBySessionToken(String sessionToken);
}

