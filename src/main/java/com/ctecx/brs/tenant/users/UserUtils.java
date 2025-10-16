package com.ctecx.brs.tenant.users;

import com.ctecx.brs.tenant.email.EmailSettings;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.Random;

@Component
@Slf4j
public class UserUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int PASSWORD_LENGTH = 12;
    private static final Random random = new SecureRandom();

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    public JavaMailSender createMailSender(EmailSettings emailSettings) {
        if (emailSettings == null) {
            log.error("EmailSettings is null");
            throw new IllegalArgumentException("Email settings cannot be null");
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailSettings.getSmtpHost());
        mailSender.setPort(emailSettings.getSmtpPort());
        mailSender.setUsername(emailSettings.getSmtpUsername());
        mailSender.setPassword(emailSettings.getSmtpPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        if ("TLS".equalsIgnoreCase(emailSettings.getEncryption())) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", String.valueOf(emailSettings.getSmtpPort()));
        }
        props.put("mail.debug", "true");


        // Validate SMTP connectivity
        try {
            mailSender.testConnection();
            log.info("SMTP connection test successful for host: {}", emailSettings.getSmtpHost());
        } catch (MessagingException e) {
            log.error("SMTP connection test failed for host: {}: {}",
                    emailSettings.getSmtpHost(), e.getMessage(), e);
            throw new RuntimeException("Failed to connect to SMTP server: " + e.getMessage(), e);
        }

        return mailSender;
    }

    public void sendWelcomeEmail(JavaMailSender mailSender, String toEmail, String fullName,
                                 String username, String password, String fromEmail) {
        try {
            if (toEmail == null || toEmail.isEmpty()) {
                log.error("Invalid recipient email: {}", toEmail);
                throw new IllegalArgumentException("Recipient email cannot be null or empty");
            }
            if (fromEmail == null || fromEmail.isEmpty()) {
                log.error("Invalid sender email: {}", fromEmail);
                throw new IllegalArgumentException("Sender email cannot be null or empty");
            }

            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(fromEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
            message.setSubject("Welcome to CloudHIMS - Your Account Details");

            String htmlContent = """
                <h2>Welcome to CloudHIMS, %s!</h2>
                <p>Your account has been successfully created. Below are your login details:</p>
                <ul>
                    <li><strong>Username:</strong> %s</li>
                    <li><strong>Temporary Password:</strong> %s</li>
                </ul>
                <p>Please change your password after your first login for security purposes.</p>
                <p>If you have any questions, please contact our support team.</p>
                <p>Best regards,<br>The CloudHIMS Team</p>
                """.formatted(fullName, username, password);

            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
            log.info("Successfully sent welcome email to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send welcome email: " + e.getMessage(), e);
        }
    }

    public void sendPasswordChangeNotification(JavaMailSender mailSender, String toEmail,
                                               String fullName, String fromEmail) {
        try {
            if (toEmail == null || toEmail.isEmpty()) {
                log.error("Invalid recipient email: {}", toEmail);
                throw new IllegalArgumentException("Recipient email cannot be null or empty");
            }
            if (fromEmail == null || fromEmail.isEmpty()) {
                log.error("Invalid sender email: {}", fromEmail);
                throw new IllegalArgumentException("Sender email cannot be null or empty");
            }

            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(fromEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
            message.setSubject("CloudHIMS - Password Changed");

            String htmlContent = """
                <h2>Hello %s,</h2>
                <p>Your password has been successfully changed.</p>
                <p>If you did not initiate this change, please contact our support team immediately.</p>
                <p>Best regards,<br>The CloudHIMS Team</p>
                """.formatted(fullName);

            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
            log.info("Successfully sent password change notification to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send password change notification to {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send password change notification: " + e.getMessage(), e);
        }
    }

    public void sendPasswordResetEmail(JavaMailSender mailSender, String toEmail,
                                       String fullName, String newPassword, String fromEmail) {
        try {
            if (toEmail == null || toEmail.isEmpty()) {
                log.error("Invalid recipient email: {}", toEmail);
                throw new IllegalArgumentException("Recipient email cannot be null or empty");
            }
            if (fromEmail == null || fromEmail.isEmpty()) {
                log.error("Invalid sender email: {}", fromEmail);
                throw new IllegalArgumentException("Sender email cannot be null or empty");
            }

            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(fromEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
            message.setSubject("CloudHIMS - Password Reset");

            String htmlContent = """
                <h2>Hello %s,</h2>
                <p>Your password has been reset. Below is your new temporary password:</p>
                <ul>
                    <li><strong>Temporary Password:</strong> %s</li>
                </ul>
                <p>Please log in with this password and change it immediately for security purposes.</p>
                <p>If you did not request this reset, please contact our support team immediately.</p>
                <p>Best regards,<br>The CloudHIMS Team</p>
                """.formatted(fullName, newPassword);

            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
            log.info("Successfully sent password reset email to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage(), e);
        }
    }
}