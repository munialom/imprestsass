package com.ctecx.brs.tenant.apisettings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingGroupDto {
    private SettingType settingType;
    private List<SettingDto> settings;
}