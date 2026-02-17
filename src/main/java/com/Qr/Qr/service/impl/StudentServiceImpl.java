package com.Qr.Qr.service.impl;

import com.Qr.Qr.dto.request.StudentRegistrationRequest;
import com.Qr.Qr.dto.response.StudentResponse;
import com.Qr.Qr.exception.EmailAlreadyExistsException;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.mapper.StudentMapper;
import com.Qr.Qr.model.Student;
import com.Qr.Qr.model.User;
import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.repository.DepartmentRepository;
import com.Qr.Qr.repository.StudentRepository;
import com.Qr.Qr.repository.UserRepository;
import com.Qr.Qr.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public StudentResponse registerStudent(StudentRegistrationRequest request){
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");

        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.STUDENT);
        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        student.setStudentId(request.getStudentId());
        student.setDepartment(departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        student.setYearOfStudy(request.getYearOfStudy());
        student.setPhoneNumber(request.getPhoneNumber());
        Student savedStudent = studentRepository.save(student);

        return studentMapper.toResponse(savedStudent);
    }

}
