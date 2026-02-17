package com.Qr.Qr.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRegistrationRequest {
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8,message = "Should have more than 8 characters")
    private String password;

    @NotBlank(message = "FullName is required")
    private String fullName;

    private String phoneNumber;
    @NotNull(message = "DepartmentId is required")
    private Long departmentId;

    @NotBlank(message = "EmployeeId is required")
    private String employeeId;
}
