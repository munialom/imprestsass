package com.ctecx.brs.tenant.users;

import com.ctecx.brs.tenant.config.TenantJdbcTemplateConfig;
import com.ctecx.brs.tenant.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordUpdateHelper {
    private static final Logger log = LoggerFactory.getLogger(PasswordUpdateHelper.class);
    
    private final UserRepository userRepository;
    private final TenantJdbcTemplateConfig tenantJdbcTemplateConfig;
    private final PasswordEncoder passwordEncoder;

    private JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = tenantJdbcTemplateConfig.getTenantJdbcTemplate();
        if (jdbcTemplate == null) {
            log.error("JdbcTemplate is null");
            throw new IllegalStateException("JdbcTemplate is not available");
        }
        return jdbcTemplate;
    }

    @Transactional
    public void changePassword(String username, PasswordChangeDTO passwordChangeDTO) {
        log.info("Changing password for user: {}", username);

        // Validate password match
        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            log.error("New password and confirm password do not match for user: {}", username);
            throw new IllegalStateException("New password and confirm password do not match");
        }

        // Fetch user to validate current password
        Optional<User> userOptional = userRepository.findByUserName(username);
        if (userOptional.isEmpty()) {
            log.error("User not found with username: {}", username);
            throw new EntityNotFoundException("User with username " + username + " not found");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            log.error("Current password is incorrect for user: {}", username);
            throw new IllegalStateException("Current password is incorrect");
        }

        // Update password using native SQL
        String encodedPassword = passwordEncoder.encode(passwordChangeDTO.getNewPassword());
        String sql = "UPDATE tbl_user SET password = ?, last_modified_date = CURRENT_TIMESTAMP, modified_by = ? WHERE user_name = ?";
        int rowsAffected = getJdbcTemplate().update(sql, encodedPassword, "system", username);

        if (rowsAffected == 0) {
            log.error("Failed to update password for user: {}", username);
            throw new IllegalStateException("Failed to update password");
        }

        log.info("Password changed successfully for user: {}", username);
    }
}