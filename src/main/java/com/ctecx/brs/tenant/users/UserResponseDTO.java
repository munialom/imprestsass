// UserResponseDTO.java
package com.ctecx.brs.tenant.users;

import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserResponseDTO {
    private Long userId;
    private String fullName;
    private String gender;
    private String userName;
    private String status;
    private boolean enabled;
    private Set<RoleDTO> roles;

    @Data
    public static class RoleDTO {
        private Long roleId;
        private String roleName;
        private String roleDescription;
    }

    public UserResponseDTO(User user) {
        this.userId = user.getUserId();
        this.fullName = user.getFullName();
        this.gender = user.getGender();
        this.userName = user.getUserName();
        this.status = user.getStatus();
        this.enabled = user.isEnabled();
        this.roles = user.getRoles().stream()
                .map(role -> {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setRoleId(role.getRoleId());
                    roleDTO.setRoleName(role.getRoleName());
                    roleDTO.setRoleDescription(role.getRoleDescription());
                    return roleDTO;
                })
                .collect(Collectors.toSet());
    }
}