package com.Qr.Qr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QrGenerationResponse {
    private Long sessionId;
    private String sessionToken;
    private String sessionName;

    private String qrCodeImage;
    private Long courseId;
    private String courseCode;
    private String courseName;

    private LocalDateTime startTime;
    private LocalDateTime expiryTime;
    private Integer durationMinutes;

    private Boolean isActive;
    private Integer totalScans;


}
