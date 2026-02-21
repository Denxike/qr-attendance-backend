package com.Qr.Qr.service.impl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.mapper.CourseMapper;
import com.Qr.Qr.model.*;
import com.Qr.Qr.repository.*;
import com.Qr.Qr.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final StudentRepository studentRepository;
    @Override
    public List<CourseResponse> getCoursesByTeacher(Long teacherId) {
        log.info("Fetching courses for teacher: {}", teacherId);

        List<Course> courses = courseRepository
                .findByTeacher_IdAndIsActive(teacherId, true);

        return courseMapper.toResponseList(courses);
    }

    @Override
    public List<CourseResponse> getCoursesByStudent(Long studentId) {
        log.info("Fetching courses for student: {}", studentId);

        List<Course> courses = courseRepository
                .findCoursesByStudentId(studentId);

        return courseMapper.toResponseList(courses);
    }

    @Override
    public CourseResponse getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course", "id", courseId));

        return courseMapper.toResponse(course);
    }
   @Override
public List<CourseResponse> getAvailableCourses(Long studentId) {
    Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
    
    Department department = student.getDepartment();
    if (department == null) {
        return Collections.emptyList();
    }
    
    List<Course> departmentCourses = courseRepository.findByDepartmentId(department.getId());
    
    // Filter out courses already enrolled
    Set<Long> enrolledCourseIds = student.getEnrollments().stream()
            .map(enrollment -> enrollment.getCourse().getId())
            .collect(Collectors.toSet());
    
return departmentCourses.stream()
            .filter(course -> !enrolledCourseIds.contains(course.getId()))
            .map(course -> {
                CourseResponse response = new CourseResponse();
                response.setId(course.getId());
                response.setCourseCode(course.getCourseCode());
                response.setCourseName(course.getCourseName());
                response.setDescription(course.getDescription());
                response.setCredits(course.getCredits());
                response.setSemester(course.getSemester());
                response.setTeacherId(course.getTeacher() != null ? course.getTeacher().getId() : null);
                response.setDepartmentId(course.getDepartment() != null ? course.getDepartment().getId() : null);
                response.setActive(course.getIsActive());
                return response;
            })
            .collect(Collectors.toList());
}
}
