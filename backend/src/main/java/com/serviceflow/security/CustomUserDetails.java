package com.serviceflow.security;

import com.serviceflow.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adapts our {@link User} entity to Spring Security's {@link UserDetails} contract.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long    id;
    private final String  name;
    private final String  email;
    private final String  password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id          = user.getId();
        this.name        = user.getName();
        this.email       = user.getEmail();
        this.password    = user.getPassword();
        this.active      = Boolean.TRUE.equals(user.getActive());
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    // ── UserDetails contract ──────────────────────────────────
    @Override public String getUsername()                    { return email; }
    @Override public String getPassword()                    { return password; }
    @Override public boolean isAccountNonExpired()           { return true; }
    @Override public boolean isAccountNonLocked()            { return active; }
    @Override public boolean isCredentialsNonExpired()       { return true; }
    @Override public boolean isEnabled()                     { return active; }
}
