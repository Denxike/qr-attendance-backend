package com.Qr.Qr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;

    @Builder.Default
    private String type = "Bearer";

    private Long userId;
    private String email;
    private String fullName;
    private String role;
    private Long studentId;
    private Long teacherId;
}
