package com.Qr.Qr.service;

import com.Qr.Qr.dto.response.QrGenerationResponse;
import com.Qr.Qr.model.QRSession;

public interface QrCodeService {
    QrGenerationResponse getSessionDetails(Long sessionId);
    QRSession generateQRCode(Long courseId, Long teacherId, String sessionName, Integer duration);
    boolean validateQRToken(String token);
    QRSession getSessionByToken(String token);
    void deactivateSession(Long sessionId, Long teacherId);
}
