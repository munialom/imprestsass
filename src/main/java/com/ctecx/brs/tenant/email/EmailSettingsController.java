package com.ctecx.brs.tenant.email;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email-settings")
@RequiredArgsConstructor
public class EmailSettingsController {
    private final EmailSettingsService emailSettingsService;

    @PostMapping
    public ResponseEntity<EmailSettings> createEmailSettings(@Valid @RequestBody EmailSettingsDTO emailSettingsDTO) {
        EmailSettings savedEmailSettings = emailSettingsService.createEmailSettings(emailSettingsDTO);
        return new ResponseEntity<>(savedEmailSettings, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailSettings> getEmailSettingsById(@PathVariable Long id) {
        EmailSettings emailSettings = emailSettingsService.getEmailSettingsById(id);
        return new ResponseEntity<>(emailSettings, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmailSettings> updateEmailSettings(@PathVariable Long id, @Valid @RequestBody EmailSettingsDTO emailSettingsDTO) {
        EmailSettings updatedEmailSettings = emailSettingsService.updateEmailSettings(id, emailSettingsDTO);
        return new ResponseEntity<>(updatedEmailSettings, HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}