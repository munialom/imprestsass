package com.ctecx.brs.tenant.apisettings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    List<SystemSetting> findBySettingType(SettingType settingType);
    Optional<SystemSetting> findBySettingTypeAndSettingName(SettingType settingType, String settingName);
    boolean existsBySettingTypeAndSettingName(SettingType settingType, String settingName);
    void deleteBySettingTypeAndSettingName(SettingType settingType, String settingName);
}