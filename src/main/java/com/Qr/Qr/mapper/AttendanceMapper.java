package com.Qr.Qr.mapper;

import com.Qr.Qr.dto.response.AttendanceResponse;
import com.Qr.Qr.model.Attendance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttendanceMapper {
    public static AttendanceResponse toResponse(Attendance attendance){
        AttendanceResponse response = new AttendanceResponse();

        response.setId(attendance.getId());

        response.setStudentId(attendance.getStudent().getId());
        response.setStudentRegistrationId(attendance.getStudent().getStudentId());
        response.setStudentName(attendance.getStudent().getUser().getFullName());
        response.setSessionId(attendance.getQrSession().getId());
        response.setSessionName(attendance.getQrSession().getSessionName());
        response.setCourseId(attendance.getQrSession().getCourse().getId());
        response.setCourseCode(attendance.getQrSession().getCourse().getCourseCode());
        response.setCourseName(attendance.getQrSession().getCourse().getCourseName());

        response.setMarkedAt(attendance.getMarkedAt());
        response.setStatus(attendance.getStatus());

        return  response;
    }
    public List<AttendanceResponse> toResponseList(List<Attendance> attendances) {
        return attendances.stream()
                .map(attendance -> toResponse(attendance))
                .collect(Collectors.toList());
    }
}
