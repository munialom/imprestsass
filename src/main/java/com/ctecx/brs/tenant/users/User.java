// User.java
package com.ctecx.brs.tenant.users;


import com.ctecx.brs.tenant.userroles.UserRole;
import com.ctecx.brs.tenant.util.AuditableBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends AuditableBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Size(max = 100)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Size(max = 10)
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Size(max = 50)
    @Column(name = "user_name", nullable = false, unique = true, length = 50)
    private String userName;

    @Size(max = 100)
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Size(max = 10)
    @Column(name = "status", nullable = false, length = 10)
    private String status;

    private String phoneNumber;

    private boolean enabled;

    @ManyToMany(fetch =FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private Set<UserRole> roles = new HashSet<>();
}