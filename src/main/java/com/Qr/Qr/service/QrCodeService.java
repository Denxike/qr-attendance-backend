package com.Qr.Qr.service;

import com.Qr.Qr.dto.request.QrGenerationRequest;
import com.Qr.Qr.dto.response.QrGenerationResponse;

public interface QrCodeService {
    QrGenerationResponse generateQRCode(QrGenerationRequest request, Long teacherId);

    QrGenerationResponse getSessionDetails(Long sessionId);

    void deactivateSession(Long sessionId, Long teacherId);
}
