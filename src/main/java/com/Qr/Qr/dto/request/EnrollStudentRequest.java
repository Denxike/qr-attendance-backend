package com.Qr.Qr.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollStudentRequest {
    @NotNull(message = "Student is required")
    private Long studentId;

    @NotNull(message = "Course is required")
    private Long courseId;
}
