package com.ctecx.brs.tenant.apisettings;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SystemSettingController {

    private final SystemSettingService service;

    @GetMapping("/{settingType}")
    public SettingGroupDto getSettings(@PathVariable SettingType settingType) {
        return service.getSettingsByType(settingType);
    }

    @PostMapping("/{settingType}")
    public void
    saveSetting(
            @PathVariable SettingType settingType,
            @RequestBody SettingDto dto
    ) {
        service.saveSetting(settingType, dto);
    }

    @DeleteMapping("/{settingType}/{settingName}")
    public void deleteSetting(
            @PathVariable SettingType settingType,
            @PathVariable String settingName
    ) {
        service.deleteSetting(settingType, settingName);
    }
}