// PasswordResetEvent.java
package com.ctecx.brs.tenant.users;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PasswordResetEvent extends ApplicationEvent {
    private final String email;
    private final String fullName;
    private final String newPassword;
    private final String fromEmail;

    public PasswordResetEvent(Object source, String email, String fullName, String newPassword, String fromEmail) {
        super(source);
        this.email = email;
        this.fullName = fullName;
        this.newPassword = newPassword;
        this.fromEmail = fromEmail;
    }
}