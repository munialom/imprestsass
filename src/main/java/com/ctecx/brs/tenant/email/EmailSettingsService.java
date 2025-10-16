package com.ctecx.brs.tenant.email;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSettingsService {
    private final EmailSettingsRepository emailSettingsRepository;

    public EmailSettings createEmailSettings(EmailSettingsDTO emailSettingsDTO) {
        EmailSettings emailSettings = new EmailSettings();
        emailSettings.setSmtpHost(emailSettingsDTO.getSmtpHost());
        emailSettings.setSmtpPort(emailSettingsDTO.getSmtpPort());
        emailSettings.setSmtpUsername(emailSettingsDTO.getSmtpUsername());
        emailSettings.setSmtpPassword(emailSettingsDTO.getSmtpPassword());
        emailSettings.setFromEmail(emailSettingsDTO.getFromEmail());
        emailSettings.setEncryption(emailSettingsDTO.getEncryption());
        return emailSettingsRepository.save(emailSettings);
    }

    public EmailSettings getEmailSettingsById(Long id) {
        return emailSettingsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Email settings with id " + id + " not found"));
    }

    public EmailSettings updateEmailSettings(Long id, EmailSettingsDTO emailSettingsDTO) {
        EmailSettings emailSettings = getEmailSettingsById(id);
        emailSettings.setSmtpHost(emailSettingsDTO.getSmtpHost());
        emailSettings.setSmtpPort(emailSettingsDTO.getSmtpPort());
        emailSettings.setSmtpUsername(emailSettingsDTO.getSmtpUsername());
        emailSettings.setSmtpPassword(emailSettingsDTO.getSmtpPassword());
        emailSettings.setFromEmail(emailSettingsDTO.getFromEmail());
        emailSettings.setEncryption(emailSettingsDTO.getEncryption());
        return emailSettingsRepository.save(emailSettings);
    }
}