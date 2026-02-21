package com.Qr.Qr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String courseCode;
    private String courseName;
    private String description;
    private Integer credits;
    private String semester;
    private Long teacherId;
    private String teacherName;
    private Long departmentId;
    private String departmentName;
    private Integer enrolledStudents;
    private Boolean isActive;
}
