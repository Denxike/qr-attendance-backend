package com.Qr.Qr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String departmentName;
    private String description;
    private Integer totalCourses;
    private Integer totalStudents;
}
