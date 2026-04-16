package com.Qr.Qr.controller;

import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.dto.response.StudentResponse;
import com.Qr.Qr.model.Course;
import com.Qr.Qr.repository.CourseRepository;
import com.Qr.Qr.repository.StudentCourseEnrollmentRepository;
import com.Qr.Qr.repository.StudentRepository;
import com.Qr.Qr.service.CourseService;
import com.Qr.Qr.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseEnrollmentRepository enrollmentRepository;

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody StudentRegistrationRequest request
    ) {
        StudentResponse response = studentService.registerStudent(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 2. Fetch all courses the student can enroll in
    @GetMapping("/{studentId}/available-courses")
    public ResponseEntity<?> getAvailableCourses(@PathVariable Long studentId) {
        List<Course> allCourses = courseRepository.findAll();

        // Use the exact same safety mapper for available courses!
        List<java.util.Map<String, Object>> safeAvailableCourses = allCourses.stream()
                .map(c -> {
                    java.util.Map<String, Object> courseData = new java.util.HashMap<>();
                    courseData.put("id", c.getId());
                    courseData.put("courseCode", c.getCourseCode());
                    courseData.put("courseName", c.getCourseName());
                    courseData.put("credits", c.getCredits());

                    if (c.getTeacher() != null && c.getTeacher().getUser() != null) {
                        courseData.put("teacherName", c.getTeacher().getUser().getFullName());
                    } else {
                        courseData.put("teacherName", "Not Assigned");
                    }

                    if (c.getDepartment() != null) {
                        courseData.put("departmentName", c.getDepartment().getDepartmentName());
                    } else {
                        courseData.put("departmentName", "N/A");
                    }
                    return courseData;
                })
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(safeAvailableCourses);
    }

    // 3. Fetch courses the student is ALREADY enrolled in
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<?> getEnrolledCourses(@PathVariable Long studentId) {

        java.util.Optional<com.Qr.Qr.model.Student> studentOpt = studentRepository.findByUserId(studentId);

        if (studentOpt.isEmpty() || studentOpt.get().getEnrollments() == null) {
            return ResponseEntity.ok(List.of());
        }

        // THE FIX: Map the raw Database Entities into safe, flat JSON Maps to prevent Infinite Recursion!
        List<java.util.Map<String, Object>> safeEnrolledCourses = studentOpt.get().getEnrollments().stream()
                .map(enrollment -> {
                    Course c = enrollment.getCourse();
                    java.util.Map<String, Object> courseData = new java.util.HashMap<>();

                    courseData.put("id", c.getId());
                    courseData.put("courseCode", c.getCourseCode());
                    courseData.put("courseName", c.getCourseName());
                    courseData.put("credits", c.getCredits());

                    // Safely get Teacher Name (Prevents NullPointerExceptions)
                    if (c.getTeacher() != null && c.getTeacher().getUser() != null) {
                        courseData.put("teacherName", c.getTeacher().getUser().getFullName());
                    } else {
                        courseData.put("teacherName", "Not Assigned");
                    }

                    // Safely get Department Name
                    if (c.getDepartment() != null) {
                        courseData.put("departmentName", c.getDepartment().getDepartmentName());
                    } else {
                        courseData.put("departmentName", "N/A");
                    }

                    return courseData;
                })
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(safeEnrolledCourses);
    }

    // 4. Enroll a student using the explicit Join Entity
    @PostMapping("/{studentId}/enroll/{courseId}")
    @Transactional // 👈 Add this here too!
    public ResponseEntity<?> enrollStudent(@PathVariable Long studentId, @PathVariable Long courseId) {

        java.util.Optional<com.Qr.Qr.model.Student> studentOpt = studentRepository.findByUserId(studentId);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("message", "Student profile not found."));
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        com.Qr.Qr.model.StudentCourseEnrollment newEnrollment = new com.Qr.Qr.model.StudentCourseEnrollment();
        newEnrollment.setStudent(studentOpt.get());
        newEnrollment.setCourse(course);

        // 👇 FIX: If your model requires a date, you MUST set it before saving!
        // newEnrollment.setEnrollmentDate(java.time.LocalDateTime.now());

        enrollmentRepository.save(newEnrollment);

        return ResponseEntity.ok(java.util.Map.of("message", "Successfully enrolled in " + course.getCourseName()));
    }
}