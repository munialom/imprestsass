package com.ctecx.brs.tenant.apisettings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingDto {
    private String settingName;
    private String settingValue;
    private boolean isSecret;
}