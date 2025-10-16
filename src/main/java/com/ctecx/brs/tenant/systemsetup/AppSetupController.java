package com.ctecx.brs.tenant.systemsetup;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/settings")
public class AppSetupController {
    private final AppSetupService appSetupService;

    public AppSetupController(AppSetupService appSetupService) {
        this.appSetupService = appSetupService;
    }


    @GetMapping("/system")
    public String setData(Model model) {
        List<AppSetup> appSetups = appSetupService.appSetupList();

        for (AppSetup appSetup : appSetups) {

            model.addAttribute(appSetup.getKey(), appSetup.getValue());
        }

        return "configs/my-school";

    }




    private void updateSettingValuesFromForm(HttpServletRequest request, List<AppSetup> settingsList) {

        for (AppSetup appSetup : settingsList) {
            String value = request.getParameter(appSetup.getKey());
            if (value != null) {
                appSetup.setValue(value);
            }
        }
        appSetupService.saveAll(settingsList);
    }



    private void saveSiteLogo(MultipartFile multipartFile, SmsSetting settingBag) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;
            settingBag.updateSiteLogo(value);

        }
    }
    @PostMapping("/mailTemplate")
    public String saveMailTemplate(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        List<AppSetup> temp = appSetupService.mailTemplateSettings();
        updateSettingValuesFromForm(request, temp);
        redirectAttributes.addFlashAttribute("message", "Email Template Successfully Updated");

        return "redirect:/configs";
    }

    @PostMapping("/mailServerSettings")
    public String SaveMailServerSettings(HttpServletRequest request) {
        List<AppSetup> mailServer = appSetupService.mailServerSettings();
        updateSettingValuesFromForm(request, mailServer);
        return "redirect:/configs";
    }


    @PostMapping("/sms")
    public String SaveMailSmsSettings(HttpServletRequest request) {
        List<AppSetup> sms = appSetupService.smsServerSettings();
        updateSettingValuesFromForm(request, sms);
        return "redirect:/configs";
    }


    @PostMapping("/school")
    public String SaveSchoolSettings(HttpServletRequest request) {
        List<AppSetup> school = appSetupService.schoolServerSettings();
        updateSettingValuesFromForm(request, school);
        return "redirect:/configs";
    }


    @PostMapping("/nssf")
    public String updateNssf(HttpServletRequest request) {
        List<AppSetup> nssfMappings = appSetupService.nssfMappings();
        updateSettingValuesFromForm(request, nssfMappings);
        return "redirect:/configs";
    }


    @PostMapping("/levies")
    public String updateLevies(HttpServletRequest request) {
        List<AppSetup> levies = appSetupService.levies();
        updateSettingValuesFromForm(request, levies);
        return "redirect:/configs";
    }

    @PostMapping("/rads")
    public String updateRads(HttpServletRequest request) {
        List<AppSetup> levies = appSetupService.levies();
        updateSettingValuesFromForm(request, levies);
        return "redirect:/configs";
    }



    @PostMapping("/accounting")
    public String accountCodes(HttpServletRequest request) {
        List<AppSetup> pos= appSetupService.posHardware();
        updateSettingValuesFromForm(request, pos);
        return "redirect:/configs";
    }

}
