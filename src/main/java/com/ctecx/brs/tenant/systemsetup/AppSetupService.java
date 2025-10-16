package com.ctecx.brs.tenant.systemsetup;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppSetupService {
    @Autowired
    private final AppSetupRepository appSetupRepository;



    public String getSetupValueByKey(String key) {
        List<AppSetup> appSetups = appSetupList();

        Optional<String> value = appSetups.stream()
                .filter(appSetup -> appSetup.getKey().equals(key))
                .map(AppSetup::getValue)
                .findFirst();

        return value.orElse(null);
    }

    public List<AppSetup> appSetupList() {

        return (List<AppSetup>) appSetupRepository.findAll();
    }

    public void saveAll(List<AppSetup> appSetups) {

        appSetupRepository.saveAll(appSetups);

    }

    public List<AppSetup> mailServerSettings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.MAIL_SERVER);
    }

    public List<AppSetup> smsServerSettings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.SMS);
    }

    public List<AppSetup> schoolServerSettings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.SCHOOL);
    }


    public List<AppSetup> nssfMappings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.NSSF);
    }

    public List<AppSetup> levies() {

        return appSetupRepository.findBySetupCategory(SetupCategory.LEVIES);
    }

    public List<AppSetup> rads() {

        return appSetupRepository.findBySetupCategory(SetupCategory.RADS);
    }

    public List<AppSetup> posHardware() {

        return appSetupRepository.findBySetupCategory(SetupCategory.FEES);
    }

    public List<AppSetup> mailTemplateSettings() {

        return appSetupRepository.findBySetupCategory(SetupCategory.MAIL_TEMPLATE);
    }

    public EmailSetting getEmailSettings() {
        List<AppSetup> allGeneralSettings = appSetupRepository.findBySetupCategory(SetupCategory.MAIL_SERVER);
        allGeneralSettings.addAll(appSetupRepository.findBySetupCategory(SetupCategory.MAIL_TEMPLATE));

        return new EmailSetting(allGeneralSettings);
    }

    public SmsSetting getSmsSetting() {
        List<AppSetup> allGeneralSettings = appSetupRepository.findBySetupCategory(SetupCategory.SMS);
        //allGeneralSettings.addAll(appSetupRepository.findBySetupCategory(SetupCategory.MAIL_TEMPLATE));

        return new SmsSetting(allGeneralSettings);
    }
}
