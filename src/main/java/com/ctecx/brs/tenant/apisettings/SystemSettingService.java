package com.ctecx.brs.tenant.apisettings;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemSettingService {

    private final SystemSettingRepository repository;

    public SettingGroupDto getSettingsByType(SettingType settingType) {
        List<SystemSetting> settings = repository.findBySettingType(settingType);
        return SettingGroupDto.builder()
                .settingType(settingType)
                .settings(settings.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void saveSetting(SettingType settingType, SettingDto dto) {
        SystemSetting setting = repository.findBySettingTypeAndSettingName(settingType, dto.getSettingName())
                .orElse(SystemSetting.builder()
                        .settingType(settingType)
                        .settingName(dto.getSettingName())
                        .build());

        setting.setSettingValue(dto.getSettingValue());
        setting.setSecret(dto.isSecret());
        repository.save(setting);
    }

    @Transactional
    public void deleteSetting(SettingType settingType, String settingName) {
        repository.deleteBySettingTypeAndSettingName(settingType, settingName);
    }

    private SettingDto convertToDto(SystemSetting setting) {
        return SettingDto.builder()
                .settingName(setting.getSettingName())
                .settingValue(setting.getSettingValue())
                .isSecret(setting.isSecret())
                .build();
    }
}