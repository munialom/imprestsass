package com.ctecx.brs.tenant.mappers;



import com.ctecx.brs.tenant.userroles.UserRole;
import com.ctecx.brs.tenant.users.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class UserMapper {

    public Optional<User> mapToUser(Map<String, Object> row) {
        return Optional.ofNullable(row)
                .map(this::createUser);
    }

    private User createUser(Map<String, Object> row) {
        var user = new User();

        // Basic fields
        user.setUserId((Long) row.get("user_id"));
        user.setFullName((String) row.get("full_name"));
        user.setGender((String) row.get("gender"));
        user.setUserName((String) row.get("user_name"));
        user.setPassword((String) row.get("password"));
        user.setStatus((String) row.get("status"));

        // Boolean field
        Optional.ofNullable(row.get("enabled"))
                .map(this::convertToBoolean)
                .ifPresent(user::setEnabled);

        // Optional phone number
        Optional.ofNullable((String) row.get("phoneNumber"))
                .ifPresent(user::setPhoneNumber);

        // Audit fields
        Optional.ofNullable((String) row.get("created_by"))
                .ifPresent(user::setCreatedBy);

        Optional.ofNullable((String) row.get("modified_by"))
                .ifPresent(user::setModifiedBy);

        Optional.ofNullable((LocalDateTime) row.get("created_date"))
                .ifPresent(user::setCreated);

        Optional.ofNullable((LocalDateTime) row.get("last_modified_date"))
                .ifPresent(user::setModified);

        // Map roles
        user.setRoles(mapRoles(row));

        return user;
    }

    private Set<UserRole> mapRoles(Map<String, Object> row) {
        String roleIds = (String) row.get("role_ids");
        String roleNames = (String) row.get("role_names");
        String roleDescriptions = (String) row.get("role_descriptions");

        if (roleIds == null || roleIds.trim().isEmpty()) {
            return new HashSet<>();
        }

        String[] idArray = roleIds.split(",");
        String[] nameArray = roleNames != null ? roleNames.split(",") : new String[0];
        String[] descArray = roleDescriptions != null ? roleDescriptions.split(",") : new String[0];

        Set<UserRole> roles = new HashSet<>();

        for (int i = 0; i < idArray.length; i++) {
            UserRole role = new UserRole();
            role.setRoleId(Long.parseLong(idArray[i].trim()));

            if (i < nameArray.length) {
                role.setRoleName(nameArray[i].trim());
            }

            if (i < descArray.length) {
                role.setRoleDescription(descArray[i].trim());
            }

            roles.add(role);
        }

        return roles;
    }

    private boolean convertToBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            return Boolean.parseBoolean(stringValue) || "1".equals(stringValue);
        }
        return false;
    }
}