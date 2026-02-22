package com.Qr.Qr.repository;

import com.Qr.Qr.model.QRSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QRSessionRepository extends JpaRepository<QRSession, Long> {
    Optional<QRSession> findBySessionToken(String sessionToken);
    boolean existsBySessionToken(String sessionToken);
    List<QRSession> findByCourse_Id(Long courseId);
    Optional<QRSession> findByCourseIdAndIsActiveTrue(Long courseId);
}
