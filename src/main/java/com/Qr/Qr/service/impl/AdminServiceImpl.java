package com.Qr.Qr.service.impl;

import com.Qr.Qr.dto.request.CreateCourseRequest;
import com.Qr.Qr.dto.request.CreateDepartmentRequest;
import com.Qr.Qr.dto.request.EnrollStudentRequest;
import com.Qr.Qr.dto.response.*;
import com.Qr.Qr.exception.DuplicateAttendanceException;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.mapper.CourseMapper;
import com.Qr.Qr.model.*;
import com.Qr.Qr.model.enums.EnrollmentStatus;
import com.Qr.Qr.repository.*;
import com.Qr.Qr.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentCourseEnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::toDepartmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new RuntimeException("Department already exists: " + request.getDepartmentName());
        }

        Department department = new Department();
        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());

        Department saved = departmentRepository.save(department);
        log.info("Created department: {}", saved.getDepartmentName());
        return toDepartmentResponse(saved);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        departmentRepository.delete(department);
        log.info("Deleted department: {}", id);
    }

    // ==================== COURSES ====================

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseMapper.toResponseList(courseRepository.findAll());
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new RuntimeException("Course code already exists: " + request.getCourseCode());
        }

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", request.getTeacherId()));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setSemester(request.getSemester());
        course.setTeacher(teacher);
        course.setDepartment(department);
        course.setIsActive(true);
        course.setCreatedAt(LocalDateTime.now());

        Course saved = courseRepository.save(course);
        log.info("Created course: {}", saved.getCourseCode());
        return courseMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        courseRepository.delete(course);
        log.info("Deleted course: {}", id);
    }

    // ==================== STUDENTS ====================

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::toStudentResponse)
                .collect(Collectors.toList());
    }

    // ==================== TEACHERS ====================

    @Override
    public List<TeacherResponse> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::toTeacherResponse)
                .collect(Collectors.toList());
    }

    // ==================== ENROLLMENT ====================

    @Override
    @Transactional
    public void enrollStudent(EnrollStudentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));

        if (enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                request.getStudentId(), request.getCourseId(), EnrollmentStatus.ENROLLED)) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        StudentCourseEnrollment enrollment = new StudentCourseEnrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
        log.info("Enrolled student {} in course {}", request.getStudentId(), request.getCourseId());
    }

    @Override
    @Transactional
    public void unenrollStudent(Long studentId, Long courseId) {
        StudentCourseEnrollment enrollment = enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "studentId/courseId", studentId));

        enrollmentRepository.delete(enrollment);
        log.info("Unenrolled student {} from course {}", studentId, courseId);
    }

    // ==================== MAPPERS ====================

    private DepartmentResponse toDepartmentResponse(Department dept) {
        long totalCourses = courseRepository.countByDepartmentId(dept.getId());
        return DepartmentResponse.builder()
                .id(dept.getId())
                .departmentName(dept.getDepartmentName())
                .description(dept.getDescription())
                .totalCourses((int) totalCourses)
                .build();
    }

    private StudentResponse toStudentResponse(Student student) {
        long enrolled = enrollmentRepository.countByStudentIdAndStatus(
                student.getId(), EnrollmentStatus.ENROLLED);
        return StudentResponse.builder()
                .id(student.getId())
                .studentId(student.getStudentId())
                .fullName(student.getUser() != null ? student.getUser().getFullName() : "")
                .email(student.getUser() != null ? student.getUser().getEmail() : "")
                .departmentName(student.getDepartment() != null ? student.getDepartment().getDepartmentName() : "")
                .yearOfStudy(student.getYearOfStudy())
                .totalEnrolledCourses((int) enrolled)
                .build();
    }

    private TeacherResponse toTeacherResponse(Teacher teacher) {
        long totalCourses = courseRepository.countByTeacherId(teacher.getId());
        return TeacherResponse.builder()
                .id(teacher.getId())
                .employeeId(teacher.getEmployeeId())
                .fullName(teacher.getUser() != null ? teacher.getUser().getFullName() : "")
                .email(teacher.getUser() != null ? teacher.getUser().getEmail() : "")
                .departmentName(teacher.getDepartment() != null ? teacher.getDepartment().getDepartmentName() : "")
                .totalCourses((int) totalCourses)
                .build();
    }
}

