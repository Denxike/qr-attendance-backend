package com.Qr.Qr.mapper;

import com.Qr.Qr.dto.response.StudentResponse;
import com.Qr.Qr.dto.response.TeacherResponse;
import com.Qr.Qr.model.Student;
import com.Qr.Qr.model.Teacher;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public StudentResponse toResponse(Student student){
        StudentResponse response = new StudentResponse();
        response.setStudentId(student.getStudentId());
        response.setEmail(student.getUser().getEmail());
        response.setFullName(student.getUser().getFullName());
        response.setPhoneNumber(student.getPhoneNumber());
        response.setYearOfStudy(student.getYearOfStudy());
        response.setDepartmentName(student.getDepartment().getDepartmentName());
        return response;
    }
}
