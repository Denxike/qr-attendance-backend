package com.Qr.Qr.service.impl;

import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.model.*;
import com.Qr.Qr.repository.*;
import com.Qr.Qr.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {
    
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final QRSessionRepository qrSessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;

      @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalTeachers", teacherRepository.count());
        stats.put("totalDepartments", departmentRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalQRSessions", qrSessionRepository.count());
        stats.put("totalAttendance", attendanceRepository.count());
        stats.put("activeUsers", userRepository.findAll().stream()
            .filter(User::getIsActive)
            .count());
        return stats;
    }

     @Override
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("email", user.getEmail());
            userMap.put("fullName", user.getFullName());
            userMap.put("role", user.getRole().name());
            userMap.put("isActive", user.getIsActive());
            userMap.put("createdAt", user.getCreatedAt());
            return userMap;
        }).collect(Collectors.toList());
    }

   @Override
    public Map<String, Object> getSystemReports() {
        Map<String, Object> reports = new HashMap<>();
        
        // Attendance rate by department
        List<Map<String, Object>> attendanceByDept = new ArrayList<>();
        for (Department dept : departmentRepository.findAll()) {
            Map<String, Object> deptReport = new HashMap<>();
            deptReport.put("departmentName", dept.getDepartmentName());
            deptReport.put("totalStudents", studentRepository.findAll().stream()
                .filter(s -> s.getDepartment().getId().equals(dept.getId()))
                .count());
            deptReport.put("totalCourses", courseRepository.findAll().stream()
                .filter(c -> c.getDepartment().getId().equals(dept.getId()))
                .count());
            attendanceByDept.add(deptReport);
        }
        reports.put("attendanceByDepartment", attendanceByDept);
        
        // Recent QR sessions
        reports.put("recentSessions", qrSessionRepository.findAll().stream()
            .sorted((a, b) -> {
                LocalDateTime timeA = a.getCreatedAt() != null ? a.getCreatedAt() : LocalDateTime.MIN;
                LocalDateTime timeB = b.getCreatedAt() != null ? b.getCreatedAt() : LocalDateTime.MIN;
                return timeB.compareTo(timeA);
            })
            .limit(10)
            .map(session -> {
                Map<String, Object> sessionMap = new HashMap<>();
                sessionMap.put("sessionName", session.getSessionName());
                sessionMap.put("courseName", session.getCourse().getCourseName());
                sessionMap.put("createdAt", session.getCreatedAt());
                sessionMap.put("isActive", session.getIsActive());
                return sessionMap;
            })
            .collect(Collectors.toList()));
        
        return reports;
    }

    @Override
    @Transactional
    public void createAdmin(Map<String, String> request) {
        User user = new User();
        user.setEmail(request.get("email"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setFullName(request.get("fullName"));
        user.setRole(Role.ADMIN);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}

