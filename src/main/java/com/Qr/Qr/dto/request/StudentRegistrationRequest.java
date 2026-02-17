package com.Qr.Qr.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegistrationRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password should be blank")
    @Size(min = 8,message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "StudentId should not be blank")
    @Pattern(regexp = "^[A-Z]{1,4}\\d{2}/\\d{5}/\\d{2}",
            message = "Invalid StudentId format")
    private String studentId;
    @NotNull(message = "Department is required")
    private Long departmentId;
    private Integer yearOfStudy;

    private String phoneNumber;

}
