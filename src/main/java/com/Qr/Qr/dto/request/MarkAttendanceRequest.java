package com.Qr.Qr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkAttendanceRequest {
    @NotBlank(message = "StudentId is required")
    @Pattern(regexp = "^[A-Z]{1,3}\\d{2}/\\d{5}/\\d{2}")
    private String studentId;
    private String qrToken;
    private String qrSessionId;
    private LocalDateTime markedAt;
    private String status;
}
