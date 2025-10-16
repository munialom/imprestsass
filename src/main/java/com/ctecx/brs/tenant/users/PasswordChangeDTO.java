// PasswordChangeDTO.java
package com.ctecx.brs.tenant.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDTO {
    @NotBlank(message = "Current password cannot be blank")
    private String currentPassword;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, message = "New password must be at least 8 characters")
    private String newPassword;

    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;
}