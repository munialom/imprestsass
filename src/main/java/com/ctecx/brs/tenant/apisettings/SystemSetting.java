package com.ctecx.brs.tenant.apisettings;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "system_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettingType settingType;

    @Column(name = "setting_name", nullable = false, length = 128)
    private String settingName;

    @Column(nullable = false, length = 1024)
    private String settingValue;

    @Column(nullable = false)
    private boolean isSecret;
}