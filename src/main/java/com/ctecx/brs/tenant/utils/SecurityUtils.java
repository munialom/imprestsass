package com.ctecx.brs.tenant.utils;


import com.ctecx.brs.tenant.users.User;
import com.ctecx.brs.tenant.users.UserRepository;
import com.ctecx.brs.tenant.util.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {
    private static UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        SecurityUtils.userRepository = userRepository;
    }

    public static Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new UserNotFoundException("Unexpected principal type");
        }

        Optional<User> user = userRepository.findByUserName(email);
        if (user == null) {
            throw new UserNotFoundException("User not found: " + email);
        }
        return user;
    }




}