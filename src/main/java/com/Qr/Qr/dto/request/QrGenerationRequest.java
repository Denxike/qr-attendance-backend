package com.Qr.Qr.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrGenerationRequest {
    private Long courseId;
    @Min(value=1,message="It should be more than 1 minute")
    @Max(value = 60,message ="It should not exceed 60 minutes")
    private Integer durationMinutes;
    private String sessionName;
}
