package com.ctecx.brs.tenant.users;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private final String email;
    private final String fullName;
    private final String username;
    private final String password;
    private final String fromEmail;

    public UserCreatedEvent(Object source, String email, String fullName, String username,
                            String password, String fromEmail) {
        super(source);
        this.email = email;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.fromEmail = fromEmail;
    }
}