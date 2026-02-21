package com.Qr.Qr.service;

import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getCoursesByTeacher(Long teacherId);
    List<CourseResponse> getAvailableCourses(Long studentId);
    List<CourseResponse> getCoursesByStudent(Long studentId);

    CourseResponse getCourseById(Long courseId);
}
