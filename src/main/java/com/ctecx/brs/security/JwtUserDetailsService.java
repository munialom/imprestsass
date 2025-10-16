package com.ctecx.brs.security;


import com.ctecx.brs.tenant.centralapp.CustomBrsRepository;
import com.ctecx.brs.tenant.userroles.UserRole;
import com.ctecx.brs.tenant.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {


    @Autowired
     private CustomBrsRepository customBrsRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       Optional<User> userOptional = customBrsRepository.getUserByUsername(userName);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Invalid user name or password: " + userName));

        System.out.println("Found user: " + user.getUserName());
        System.out.println("User roles set is null? " + (user.getRoles() == null));
        System.out.println("User roles size: " + (user.getRoles() != null ? user.getRoles().size() : "N/A"));

        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {

            });
        }

        List<SimpleGrantedAuthority> authorities = getAuthorities(user);
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            System.out.println("Roles set is null or empty for user: " + user.getUserName());
            return List.of(new SimpleGrantedAuthority("ROLE_USER")); // Default role
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .filter(Objects::nonNull)
                .map(UserRole::getRoleName)
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        System.out.println("Authorities for user " + user.getUserName() + ": " + authorities);
        return authorities.isEmpty() ?
                List.of(new SimpleGrantedAuthority("ROLE_USER")) :
                authorities;
    }
}