package com.ctecx.brs.tenant.users;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PasswordChangedEvent extends ApplicationEvent {
    private final String email;
    private final String fullName;
    private final String fromEmail;

    public PasswordChangedEvent(Object source, String email, String fullName, String fromEmail) {
        super(source);
        this.email = email;
        this.fullName = fullName;
        this.fromEmail = fromEmail;
    }
}