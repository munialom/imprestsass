package com.ctecx.brs.tenant.users;

import com.ctecx.brs.tenant.centralapp.CustomBrsRepository;
import com.ctecx.brs.tenant.config.TenantJdbcTemplateConfig;
import com.ctecx.brs.tenant.email.EmailSettings;
import com.ctecx.brs.tenant.userroles.UserRole;
import com.ctecx.brs.tenant.userroles.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final TenantJdbcTemplateConfig tenantJdbcTemplateConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserUtils userUtils;
    private final ApplicationEventPublisher eventPublisher;
    private final CustomBrsRepository customBrsRepository;
    private final PasswordUpdateHelper passwordUpdateHelper; // Add this

    private final UserUpdateHelper userUpdateHelper; // Add this
    private final UserRoleUpdateHelper userRoleUpdateHelper; // Add this

    public boolean checkEmailExists(String email) {
        log.info("Checking if email exists: {} ", email);
        if (email == null || email.isEmpty()) {
            log.error("Email is null or empty");
            return false;
        }
        String sql = "SELECT COUNT(*) FROM tbl_user WHERE user_name = ?";
        try {
            Integer count = getJdbcTemplate().queryForObject(sql, new Object[]{email}, Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("Error checking email existence for {}: {}", email, e.getMessage(), e);
            return false;
        }
    }

    @Transactional
    public void resetPassword(String email) {
        log.info("Initiating password reset for email:",
                email);
        if (email == null || email.isEmpty()) {
            log.error("Email is null or empty");
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // Check if user exists and get full_name
        String sql = "SELECT full_name FROM tbl_user WHERE user_name = ?";
        String fullName;
        try {
            fullName = getJdbcTemplate().queryForObject(sql, new Object[]{email}, String.class);
        } catch (Exception e) {
            log.error("User with email {} not found ", email);
            throw new IllegalArgumentException("User with email " + email + " not found");
        }

        // Generate new password and update
        String newPassword = userUtils.generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);
        String updateSql = "UPDATE tbl_user SET password = ? WHERE user_name = ?";
        int rowsAffected = getJdbcTemplate().update(updateSql, encodedPassword, email);
        if (rowsAffected == 0) {
            log.error("Failed to update password for email: {}",
                    email);
            throw new IllegalStateException("Failed to reset password");
        }
        log.info("Password reset successfully for user: {} ", email);

        // Publish password reset event
        String fromEmail = getFromEmail();
        if (fromEmail != null) {
            log.info("Publishing PasswordResetEvent for user: {}", email);
            eventPublisher.publishEvent(new PasswordResetEvent(
                    this,
                    email,
                    fullName,
                    newPassword,
                    fromEmail
            ));
        } else {
            log.warn("No from email configured, skipping password reset notification for user: {}", email);
        }
    }

    private String getFromEmail() {
        String sql = "SELECT from_email FROM tbl_email_settings ORDER BY email_settings_id DESC LIMIT 1";
        try {
            return getJdbcTemplate().queryForObject(sql, String.class);
        } catch (Exception e) {
            log.error("Failed to fetch from email: {}", e.getMessage(), e);
            return null;
        }
    }

    @Transactional
    public User updateUserProfile(String username, UserProfileDTO userProfileDTO) {
        Optional<User> userOptional = customBrsRepository.getUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFullName(userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName());
            user.setUserName(userProfileDTO.getEmail());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }


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

    public User getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return customBrsRepository.getUserByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with username {} not found", username);
                    return new EntityNotFoundException("User with username " + username + " not found");
                });
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

    @Transactional
    public User createUser(UserDTO userDTO) {
        log.info("Creating user with username: {}", userDTO.getUserName());

        // Validate username uniqueness
        Optional<User> existingUser = userRepository.findByUserName(userDTO.getUserName());
        if (existingUser.isPresent()) {
            log.error("User with username {} already exists", userDTO.getUserName());
            throw new IllegalStateException("A user with username '" + userDTO.getUserName() + "' already exists.");
        }

        // Validate email format
        if (!isValidEmail(userDTO.getUserName())) {
            log.error("Invalid email format for username: {}", userDTO.getUserName());
            throw new IllegalArgumentException("Username must be a valid email address");
        }

        // Generate random password if not provided
        String password = userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()
                ? userDTO.getPassword()
                : userUtils.generateRandomPassword();
        log.info("Generated password for user {}: {}", userDTO.getUserName(), password);

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setGender(userDTO.getGender());
        user.setUserName(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(userDTO.getStatus());
        user.setEnabled(userDTO.isEnabled());

        // Assign roles
        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            Set<UserRole> roles = userDTO.getRoleIds().stream()
                    .map(userRoleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            if (roles.size() != userDTO.getRoleIds().size()) {
                log.error("One or more roles not found for role IDs: {}", userDTO.getRoleIds());
                throw new EntityNotFoundException("One or more roles in the provided IDs not found");
            }
            user.setRoles(roles);
            log.info("Assigned {} roles to user {}", roles.size(), userDTO.getUserName());
        }

        // Save user
        User savedUser = userRepository.save(user);
        log.info("User {} saved successfully with ID: {}", userDTO.getUserName(), savedUser.getUserId());

        // Publish user created event
        EmailSettings emailSettings = getEmailSettings();
        if (emailSettings != null) {
            log.info("Publishing UserCreatedEvent for user: {}", userDTO.getUserName());
            eventPublisher.publishEvent(new UserCreatedEvent(
                    this,
                    userDTO.getUserName(),
                    userDTO.getFullName(),
                    userDTO.getUserName(),
                    password,
                    emailSettings.getFromEmail()
            ));
        } else {
            log.warn("No email settings configured, skipping welcome email for user: {}", userDTO.getUserName());
        }

        return savedUser;
    }

    public User getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        return userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> {
                    log.error("User with id {} not found", id);
                    return new EntityNotFoundException("User with id " + id + " not found");
                });
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }
    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);

        // Update user details (excluding password)
        userUpdateHelper.updateUser(id, userDTO);

        // Update role associations
        userRoleUpdateHelper.updateUserRoles(id, userDTO.getRoleIds());

        // Re-fetch the updated user to return
        return userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> {
                    log.error("User with ID {} not found after update", id);
                    return new EntityNotFoundException("User with ID " + id + " not found after update");
                });
    }

/*    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> {
                    log.error("User with id {} not found", id);
                    return new EntityNotFoundException("User with id " + id + " not found");
                });

        user.setFullName(userDTO.getFullName());
        user.setGender(userDTO.getGender());
        user.setUserName(userDTO.getUserName());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setStatus(userDTO.getStatus());
        user.setEnabled(userDTO.isEnabled());

        if (userDTO.getRoleIds() != null) {
            Set<UserRole> roles = userDTO.getRoleIds().stream()
                    .map(userRoleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            if (roles.size() != userDTO.getRoleIds().size()) {
                log.error("One or more roles not found for role IDs: {}", userDTO.getRoleIds());
                throw new EntityNotFoundException("One or more roles in the provided IDs not found");
            }
            user.setRoles(roles);
            log.info("Updated {} roles for user {}", roles.size(), userDTO.getUserName());
        }

        User updatedUser = userRepository.save(user);
        log.info("User {} updated successfully", userDTO.getUserName());
        return updatedUser;
    }*/

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(Math.toIntExact(id))) {
            log.error("User with id {} not found", id);
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(Math.toIntExact(id));
        log.info("User with ID {} deleted successfully", id);
    }

    @Transactional
    public void changePassword(String username, PasswordChangeDTO passwordChangeDTO) {
        log.info("Initiating password change for user: {}", username);

        // Delegate to PasswordUpdateHelper
        passwordUpdateHelper.changePassword(username, passwordChangeDTO);

        // Publish password changed event
        EmailSettings emailSettings = getEmailSettings();
        if (emailSettings != null) {
            log.info("Publishing PasswordChangedEvent for user: {}", username);
            User user = getUserByUsername(username); // Fetch user for fullName
            eventPublisher.publishEvent(new PasswordChangedEvent(
                    this,
                    username,
                    user.getFullName(),
                    emailSettings.getFromEmail()
            ));
        } else {
            log.warn("No email settings configured, skipping password change notification for user: {}", username);
        }
    }



/*    @Transactional
    public void changePassword(String username, PasswordChangeDTO passwordChangeDTO) {
        log.info("Changing password for user: {}", username);

        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            log.error("New password and confirm password do not match for user: {}", username);
            throw new IllegalStateException("New password and confirm password do not match");
        }

        Optional<User> userOptional = customBrsRepository.getUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
                log.error("Current password is incorrect for user: {}", username);
                throw new IllegalStateException("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
            User updatedUser = userRepository.save(user);
            log.info("Password changed successfully for user: {}", username);

            // Publish password changed event
            EmailSettings emailSettings = getEmailSettings();
            if (emailSettings != null) {
                log.info("Publishing PasswordChangedEvent for user: {}", username);
                eventPublisher.publishEvent(new PasswordChangedEvent(
                        this,
                        username,
                        user.getFullName(),
                        emailSettings.getFromEmail()
                ));
            } else {
                log.warn("No email settings configured, skipping password change notification for user: {}", username);
            }


        } else {
            log.error("User not found with username: {}", username);
            throw new RuntimeException("User not found with username: " + username);
        }
    }*/

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}