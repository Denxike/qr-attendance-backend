package com.Qr.Qr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String email;
    private String fullName;
    private Integer yearOfStudy;
    private String phoneNumber;
    private String studentId;
    private String departmentName;
    private Integer totalEnrolledCourses;
//    private Boolean isActive;
}
