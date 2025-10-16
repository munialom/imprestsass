package com.ctecx.brs.tenant.email;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_email_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_settings_id")
    private Long emailSettingsId;

    @NotBlank(message = "SMTP host cannot be blank")
    @Size(max = 100, message = "SMTP host cannot exceed 100 characters")
    @Column(name = "smtp_host", nullable = false)
    private String smtpHost;

    @Column(name = "smtp_port", nullable = false)
    private Integer smtpPort;

    @NotBlank(message = "SMTP username cannot be blank")
    @Size(max = 100, message = "SMTP username cannot exceed 100 characters")
    @Column(name = "smtp_username", nullable = false)
    private String smtpUsername;

    @NotBlank(message = "SMTP password cannot be blank")
    @Size(max = 100, message = "SMTP password cannot exceed 100 characters")
    @Column(name = "smtp_password", nullable = false)
    private String smtpPassword;

    @NotBlank(message = "From email cannot be blank")
    @Size(max = 100, message = "From email cannot exceed 100 characters")
    @Column(name = "from_email", nullable = false)
    private String fromEmail;

    @NotBlank(message = "Encryption cannot be blank")
    @Size(max = 20, message = "Encryption cannot exceed 20 characters")
    @Column(name = "encryption", nullable = false)
    private String encryption; // e.g., SSL, TLS, None
}