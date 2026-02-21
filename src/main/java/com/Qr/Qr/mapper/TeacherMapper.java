package com.Qr.Qr.mapper;

import com.Qr.Qr.dto.request.TeacherRegistrationRequest;
import com.Qr.Qr.dto.response.TeacherResponse;
import com.Qr.Qr.model.Department;
import com.Qr.Qr.model.Teacher;
import com.Qr.Qr.model.User;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
    public Teacher toEntity(TeacherRegistrationRequest request,
                            User user,
                            Department department){
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setEmployeeId(request.getEmployeeId());
        teacher.setDepartment(department);
        teacher.setPhoneNumber(request.getPhoneNumber());

        return teacher;
    }
    public TeacherResponse toResponse(Teacher teacher){
        TeacherResponse response = new TeacherResponse();
        response.setId(teacher.getId());
        response.setEmail(teacher.getUser().getEmail());
        response.setFullName(teacher.getUser().getFullName());
        response.setDepartmentId(teacher.getDepartment().getId());
        response.setPhoneNumber(teacher.getPhoneNumber());
        response.setEmployeeId(teacher.getEmployeeId());
        return response;
    }
}
