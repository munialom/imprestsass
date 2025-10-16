package com.ctecx.brs.tenant.users;

import com.ctecx.brs.tenant.config.TenantJdbcTemplateConfig;
import com.ctecx.brs.tenant.exception.DuplicateEntityException;
import com.ctecx.brs.tenant.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * A helper component responsible for handling the update logic for a User entity.
 * This implementation uses JdbcTemplate for direct SQL execution.
 */
@Component
@RequiredArgsConstructor
public class UserUpdateHelper {
    private static final Logger log = LoggerFactory.getLogger(UserUpdateHelper.class);

    private final UserRepository userRepository;
    private final TenantJdbcTemplateConfig tenantJdbcTemplateConfig;

    /**
     * Retrieves the configured tenant-specific JdbcTemplate.
     * @return The configured JdbcTemplate instance.
     * @throws IllegalStateException if the JdbcTemplate is not available.
     */
    private JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = tenantJdbcTemplateConfig.getTenantJdbcTemplate();
        if (jdbcTemplate == null) {
            log.error("JdbcTemplate is null. Tenant context may not be properly initialized.");
            throw new IllegalStateException("JdbcTemplate is not available");
        }
        return jdbcTemplate;
    }

    /**
     * Retrieves the username of the currently authenticated principal.
     * This version is more efficient as it avoids an extra database call.
     * It retrieves the username directly from the security context.
     *
     * @return The username of the current user, or "system" if not found.
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "system";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            // Standard case: the principal is a UserDetails object.
            return ((UserDetails) principal).getUsername();
        }

        // Fallback for other principal types.
        return principal.toString();
    }

    /**
     * Updates an existing user in the database using a direct JDBC call.
     *
     * @param id The ID of the user to update.
     * @param userDTO The DTO containing the new user data.
     * @throws EntityNotFoundException if no user with the given ID is found.
     * @throws DuplicateEntityException if the new username is already taken by another user.
     * @throws IllegalStateException if the update operation affects zero rows.
     */
    @Transactional
    public void updateUser(Long id, UserDTO userDTO) {
        log.info("Attempting to update user with ID: {}", id);

        // 1. Validate that the user exists before attempting an update.
        // Note: Using Math.toIntExact suggests the User entity ID is an Integer.
        // It's recommended to standardize on Long for entity IDs.
        if (!userRepository.existsById(Math.toIntExact(id))) {
            log.error("Update failed: User with ID {} not found", id);
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }

        // 2. Validate that the new username isn't already taken by *another* user.
        Optional<User> userByUsername = userRepository.findByUserName(userDTO.getUserName());
        if (userByUsername.isPresent() && !userByUsername.get().getUserId().equals(id)) {
            log.error("Update failed: User with username {} already exists", userDTO.getUserName());
            throw new DuplicateEntityException("User with username '" + userDTO.getUserName() + "' already exists");
        }

        // 3. Prepare the native SQL query with correct placeholders.
        //    Using a placeholder for the timestamp is better practice than a native DB function.
        String sql = "UPDATE tbl_user SET " +
                "full_name = ?, " +
                "gender = ?, " +
                "user_name = ?, " +
                "status = ?, " +
                "enabled = ?, " +
                "last_modified_date = ?, " +
                "modified_by = ? " +
                "WHERE user_id = ?";

        // 4. Execute the update, providing all arguments in the correct order.
        int rowsAffected = getJdbcTemplate().update(sql,
                userDTO.getFullName(),      // Corresponds to full_name = ?
                userDTO.getGender(),        // Corresponds to gender = ?
                userDTO.getUserName(),      // Corresponds to user_name = ?
                userDTO.getStatus(),        // Corresponds to status = ?
                userDTO.isEnabled(),        // Corresponds to enabled = ?
                LocalDateTime.now(),        // FIX: Pass timestamp as a parameter.
                getCurrentUsername(),       // Corresponds to modified_by = ?
                id                          // Corresponds to WHERE user_id = ?
        );

        // 5. Verify that the update was successful.
        if (rowsAffected == 0) {
            log.error("Failed to update user with ID: {}. The query executed but affected 0 rows.", id);
            throw new IllegalStateException("Update failed for user with ID: " + id + ". The user may have been deleted by another transaction.");
        }

        log.info("User with ID {} was updated successfully. Rows affected: {}", id, rowsAffected);
    }
}