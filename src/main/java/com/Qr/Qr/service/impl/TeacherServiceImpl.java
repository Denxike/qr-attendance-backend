package com.Qr.Qr.service.impl;

import com.Qr.Qr.dto.request.TeacherRegistrationRequest;
import com.Qr.Qr.dto.response.TeacherResponse;
import com.Qr.Qr.exception.ResourceNotFoundException;
import com.Qr.Qr.mapper.TeacherMapper;
import com.Qr.Qr.model.Department;
import com.Qr.Qr.model.Student;
import com.Qr.Qr.model.Teacher;
import com.Qr.Qr.model.User;
import com.Qr.Qr.model.enums.Role;
import com.Qr.Qr.repository.DepartmentRepository;
import com.Qr.Qr.repository.TeacherRepository;
import com.Qr.Qr.repository.UserRepository;
import com.Qr.Qr.service.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Qr.Qr.exception.EmailAlreadyExistsException;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherMapper teacherMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TeacherResponse registerTeacher(TeacherRegistrationRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException(
                    "Email "+request.getEmail()+" is already registered"
            );
        }
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Department with ID "+ request.getDepartmentId()+" not found."
                ));
        User user = new User();
        user.setRole(Role.TEACHER);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setEmployeeId(request.getEmployeeId());
        teacher.setDepartment(department);
        teacher.setPhoneNumber(request.getPhoneNumber());

        Teacher savedTeacher = teacherRepository.save(teacher);

        return teacherMapper.toResponse(savedTeacher);
    }
}
