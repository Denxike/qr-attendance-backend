package com.Qr.Qr.controller;

import com.Qr.Qr.dto.request.EnrollStudentRequest;
import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.mapper.CourseMapper;
import com.Qr.Qr.model.Course;
import com.Qr.Qr.repository.CourseRepository;
import com.Qr.Qr.service.AdminService;
import com.Qr.Qr.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final AdminService adminService;

//    // Teacher gets their courses
//    @GetMapping("/teachers/{teacherId}/courses")
//    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
//    public ResponseEntity<List<CourseResponse>> getTeacherCourses(
//            @PathVariable Long teacherId) {
//        return ResponseEntity.ok(courseService.getCoursesByTeacher(teacherId));
//    }

//    @GetMapping("/students/{studentId}/courses")
//    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
//    public ResponseEntity<List<CourseResponse>> getStudentCourses(
//            @PathVariable Long studentId) {
//        return ResponseEntity.ok(courseService.getCoursesByStudent(studentId));
//    }

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<CourseResponse> getCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }
    // Student browses available courses
    @GetMapping("/students/{studentId}/available-courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponse>> getAvailableCourses(
            @PathVariable Long studentId) {
        List<Course> courses = courseRepository.findAvailableCoursesForStudent(studentId);
        return ResponseEntity.ok(courseMapper.toResponseList(courses));
    }

    // Student self-enrolls
    @PostMapping("/students/{studentId}/enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> selfEnroll(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        EnrollStudentRequest request = new EnrollStudentRequest();
        request.setStudentId(studentId);
        request.setCourseId(courseId);
        adminService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
