package com.ctecx.brs.tenant.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailSettingsRepository extends JpaRepository<EmailSettings, Long> {
    Optional<EmailSettings> findFirstByOrderByEmailSettingsIdDesc();
}