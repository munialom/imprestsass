package com.ctecx.brs.tenant.util;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "registered_computers")
@Data
public class RegisteredComputer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String computerName;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private String macAddress;

    private String description;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    private boolean active = true;
}