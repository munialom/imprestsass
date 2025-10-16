package com.ctecx.brs.tenant.users;

import com.ctecx.brs.tenant.config.TenantJdbcTemplateConfig;
import com.ctecx.brs.tenant.exception.EntityNotFoundException;
import com.ctecx.brs.tenant.userroles.UserRole;
import com.ctecx.brs.tenant.userroles.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRoleUpdateHelper {
    private static final Logger log = LoggerFactory.getLogger(UserRoleUpdateHelper.class);

    private final UserRoleRepository userRoleRepository;
    private final TenantJdbcTemplateConfig tenantJdbcTemplateConfig;

    private JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = tenantJdbcTemplateConfig.getTenantJdbcTemplate();
        if (jdbcTemplate == null) {
            log.error("JdbcTemplate is null");
            throw new IllegalStateException("JdbcTemplate is not available");
        }
        return jdbcTemplate;
    }

    @Transactional
    public void updateUserRoles(Long userId, Set<Long> roleIds) {
        log.info("Updating roles for user ID: {}", userId);

        // Validate role IDs
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<UserRole> roles = roleIds.stream()
                    .map(userRoleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            if (roles.size() != roleIds.size()) {
                log.error("One or more roles not found for role IDs: {}", roleIds);
                throw new EntityNotFoundException("One or more roles in the provided IDs not found");
            }
        }

        // Delete existing role associations
        String deleteSql = "DELETE FROM user_roles WHERE user_id = ?";
        int deletedRows = getJdbcTemplate().update(deleteSql, userId);
        log.info("Deleted {} role associations for user ID: {}", deletedRows, userId);

        // Insert new role associations
        if (roleIds != null && !roleIds.isEmpty()) {
            String insertSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
            for (Long roleId : roleIds) {
                getJdbcTemplate().update(insertSql, userId, roleId);
            }
            log.info("Inserted {} role associations for user ID: {}", roleIds.size(), userId);
        }
    }
}