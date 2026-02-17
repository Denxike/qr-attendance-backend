package com.Qr.Qr.controller;

import com.Qr.Qr.dto.request.QrGenerationRequest;
import com.Qr.Qr.dto.response.QrGenerationResponse;
import com.Qr.Qr.service.QrCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/qr")
@RequiredArgsConstructor
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QrGenerationResponse> generateQrCode (
            @Valid @RequestBody QrGenerationRequest request,
            Authentication authentication) {
        Long teacherId = getTeacherIdFromAuth(authentication);
        QrGenerationResponse response = qrCodeService.generateQRCode(request, teacherId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/session/{id}/deactivate")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deactivateSession(
            @PathVariable Long id,
            Authentication authentication) {

        Long teacherId = getTeacherIdFromAuth(authentication);
        qrCodeService.deactivateSession(id, teacherId);
        return ResponseEntity.noContent().build();
    }
    private Long getTeacherIdFromAuth(Authentication authentication) {
        return 1L;
    }
}
