package com.Qr.Qr.mapper;

import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.model.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public static CourseResponse toResponse(Course course) {
        CourseResponse response = new CourseResponse();

        response.setId(course.getId());
        response.setCourseCode(course.getCourseCode());
        response.setCourseName(course.getCourseName());
        response.setDescription(course.getDescription());
        response.setCredits(course.getCredits());
        response.setSemester(course.getSemester());
        response.setIsActive(course.getIsActive());

        // Teacher info (null check)
        if (course.getTeacher() != null) {
            response.setTeacherId(course.getTeacher().getId());
            if (course.getTeacher().getUser() != null) {
                response.setTeacherName(course.getTeacher().getUser().getFullName());
            }
        }

        // Department info (null check)
        if (course.getDepartment() != null) {
            response.setDepartmentId(course.getDepartment().getId());
            response.setDepartmentName(course.getDepartment().getDepartmentName());
        }
	response.setIsActive(course.getIsActive());  
    if (course.getEnrollments() != null) {
        response.setEnrolledStudents(course.getEnrollments().size());
    }
        return response;
    }

    public List<CourseResponse> toResponseList(List<Course> courses) {
        return courses.stream()
                .map(Coursemapper::toResponse)
                .collect(Collectors.toList());
    }
}
