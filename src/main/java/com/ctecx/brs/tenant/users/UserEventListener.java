package com.ctecx.brs.tenant.users;

import com.ctecx.brs.tenant.config.TenantJdbcTemplateConfig;
import com.ctecx.brs.tenant.email.EmailSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {
    private final TenantJdbcTemplateConfig tenantJdbcTemplateConfig;
    private final UserUtils userUtils;

    private JdbcTemplate getJdbcTemplate() {
        try {
            JdbcTemplate jdbcTemplate = tenantJdbcTemplateConfig.getTenantJdbcTemplate();
            if (jdbcTemplate == null) {
                log.error("JdbcTemplate is null");
                throw new IllegalStateException("JdbcTemplate is not available");
            }
            return jdbcTemplate;
        } catch (Exception e) {
            log.error("Failed to get JdbcTemplate: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to get JdbcTemplate: " + e.getMessage(), e);
        }
    }

    private EmailSettings getEmailSettings() {
        String sql = "SELECT email_settings_id, smtp_host, smtp_port, smtp_username, smtp_password, from_email, encryption " +
                "FROM tbl_email_settings ORDER BY email_settings_id DESC LIMIT 1";
        try {
            EmailSettings settings = getJdbcTemplate().queryForObject(sql, new EmailSettingsRowMapper());
            log.info("Fetched email settings: host={}, port={}, username={}",
                    settings.getSmtpHost(), settings.getSmtpPort(), settings.getSmtpUsername());
            return settings;
        } catch (Exception e) {
            log.error("Failed to fetch email settings: {}", e.getMessage(), e);
            return null;
        }
    }

    private static class EmailSettingsRowMapper implements RowMapper<EmailSettings> {
        @Override
        public EmailSettings mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmailSettings settings = new EmailSettings();
            settings.setEmailSettingsId(rs.getLong("email_settings_id"));
            settings.setSmtpHost(rs.getString("smtp_host"));
            settings.setSmtpPort(rs.getInt("smtp_port"));
            settings.setSmtpUsername(rs.getString("smtp_username"));
            settings.setSmtpPassword(rs.getString("smtp_password"));
            settings.setFromEmail(rs.getString("from_email"));
            settings.setEncryption(rs.getString("encryption"));
            return settings;
        }
    }

    @Async
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Handling UserCreatedEvent for email: {}", event.getEmail());
        try {
            EmailSettings emailSettings = getEmailSettings();
            if (emailSettings == null) {
                log.error("No email settings configured");
                throw new IllegalStateException("No email settings configured");
            }

            JavaMailSender mailSender = userUtils.createMailSender(emailSettings);
            userUtils.sendWelcomeEmail(mailSender,
                    event.getEmail(),
                    event.getFullName(),
                    event.getUsername(),
                    event.getPassword(),
                    event.getFromEmail());
            log.info("Successfully processed UserCreatedEvent for email: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to process UserCreatedEvent for email {}: {}",
                    event.getEmail(), e.getMessage(), e);
        }
    }

    @Async
    @EventListener
    public void handlePasswordChangedEvent(PasswordChangedEvent event) {
        log.info("Handling PasswordChangedEvent for email: {}", event.getEmail());
        try {
            EmailSettings emailSettings = getEmailSettings();
            if (emailSettings == null) {
                log.error("No email settings configured");
                throw new IllegalStateException("No email settings configured");
            }

            JavaMailSender mailSender = userUtils.createMailSender(emailSettings);
            userUtils.sendPasswordChangeNotification(mailSender,
                    event.getEmail(),
                    event.getFullName(),
                    event.getFromEmail());
            log.info("Successfully processed PasswordChangedEvent for email: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to process PasswordChangedEvent for email {}: {}",
                    event.getEmail(), e.getMessage(), e);
        }
    }


    @Async
    @EventListener
    public void handlePasswordResetEvent(PasswordResetEvent event) {
        log.info("Handling PasswordResetEvent for email: {}", event.getEmail());
        try {
            EmailSettings emailSettings = getEmailSettings();
            if (emailSettings == null) {
                log.error("No email settings configured");
                throw new IllegalStateException("No email settings configured");
            }

            JavaMailSender mailSender = userUtils.createMailSender(emailSettings);
            userUtils.sendPasswordResetEmail(
                    mailSender,
                    event.getEmail(),
                    event.getFullName(),
                    event.getNewPassword(),
                    event.getFromEmail()
            );
            log.info("Successfully processed PasswordResetEvent for email: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to process PasswordResetEvent for email {}: {}",
                    event.getEmail(), e.getMessage(), e);
        }
    }
}