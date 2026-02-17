package com.Qr.Qr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponse {
    private Long id;
    private String employeeId;  // e.g., "CS/2024/001"
    private String departmentName;
    private Long departmentId;
    private Integer totalCourses;
    private String email;
    private String fullName;  // e.g., "CS401"
    private String phoneNumber;

}
