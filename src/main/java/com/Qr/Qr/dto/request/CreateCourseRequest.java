package com.Qr.Qr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
//@NoArgsConstructor
@Data
public class CreateCourseRequest {
    @NotBlank(message = "Course code is required")
    private String courseCode;

    @NotBlank(message = "Course name is required")
    private String courseName;

    private String description;

    private Integer credits;

    private String semester;

    @NotNull(message = "Teacher is required")
    private Long teacherId;

    @NotNull(message = "Department is required")
    private Long departmentId;
}
