package com.scms.dto;

import com.scms.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "Reset token is required")
    @Size(max = 255, message = "Invalid reset token")
    private String token;

    @NotBlank(message = "New password is required")
    @ValidPassword
    private String newPassword;

}