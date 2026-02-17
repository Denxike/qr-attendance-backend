package com.Qr.Qr.service;

import com.Qr.Qr.dto.request.CreateCourseRequest;
import com.Qr.Qr.dto.request.CreateDepartmentRequest;
import com.Qr.Qr.dto.request.EnrollStudentRequest;
import com.Qr.Qr.dto.response.CourseResponse;
import com.Qr.Qr.dto.response.DepartmentResponse;
import com.Qr.Qr.dto.response.StudentResponse;
import com.Qr.Qr.dto.response.TeacherResponse;

import java.util.List;

public interface AdminService {
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse createDepartment(CreateDepartmentRequest request);
    void deleteDepartment(Long id);

    List<CourseResponse> getAllCourses();
    CourseResponse createCourse(CreateCourseRequest request);
    void deleteCourse(Long id);

    List<StudentResponse> getAllStudents();

    List<TeacherResponse> getAllTeachers();

    void enrollStudent(EnrollStudentRequest request);
    void unenrollStudent(Long studentId, Long courseId);
}
