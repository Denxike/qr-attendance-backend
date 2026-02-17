package com.Qr.Qr.mapper;

import com.Qr.Qr.dto.response.AttendanceResponse;
import com.Qr.Qr.model.Attendance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttendanceMapper {
    public AttendanceResponse toResponse(Attendance attendance){
        AttendanceResponse response = new AttendanceResponse();

        response.setId(attendance.getId());

        response.setStudentId(attendance.getStudent().getId());
        response.setStudentRegistrationId(attendance.getStudent().getStudentId());
        response.setStudentName(attendance.getStudent().getUser().getFullName());
        response.setSessionId(attendance.getId());
        response.setSessionName(attendance.getQrSession().getSessionName());
        response.setCourseId(attendance.getCourse().getId());
        response.setCourseCode(attendance.getCourse().getCourseCode());
        response.setCourseName(attendance.getCourse().getCourseName());

        response.setMarkedAt(attendance.getMarkedAt());
        response.setStatus(attendance.getStatus().name());

        return  response;
    }
    public List<AttendanceResponse> toResponseList(List<Attendance> attendances) {
        return attendances.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
