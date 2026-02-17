package com.Qr.Qr.service.impl;

import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.mapper.CourseMapper;
import com.Qr.Qr.model.Course;
import com.Qr.Qr.repository.CourseRepository;
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
}
