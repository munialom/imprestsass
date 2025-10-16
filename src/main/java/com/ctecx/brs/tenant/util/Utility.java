package com.ctecx.brs.tenant.util;


import com.ctecx.brs.tenant.systemsetup.EmailSetting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class Utility {

    public static String getSiteUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");
    }


    public static JavaMailSenderImpl prepareMailSender(EmailSetting emailSetting) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailSetting.getHost());

        mailSender.setPassword(emailSetting.getPassword());
        mailSender.setPort(emailSetting.getPortNumber());

        mailSender.setUsername(emailSetting.getUserName());

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", emailSetting.getSmtpAuth());
        props.setProperty("mail.smtp.starttls.enable", emailSetting.getSmtpSecure());
        mailSender.setJavaMailProperties(props);


        return mailSender;
    }
}
