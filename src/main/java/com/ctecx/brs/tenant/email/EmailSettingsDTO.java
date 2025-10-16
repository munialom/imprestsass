package com.ctecx.brs.tenant.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailSettingsDTO {
    @NotBlank(message = "SMTP host cannot be blank")
    @Size(max = 100)
    private String smtpHost;

    private Integer smtpPort;

    @NotBlank(message = "SMTP username cannot be blank")
    @Size(max = 100)
    private String smtpUsername;

    @NotBlank(message = "SMTP password cannot be blank")
    @Size(max = 100)
    private String smtpPassword;

    @NotBlank(message = "From email cannot be blank")
    @Size(max = 100)
    private String fromEmail;

    @NotBlank(message = "Encryption cannot be blank")
    @Size(max = 20)
    private String encryption;
}