package com.personalproject.user_service.security.config;


import com.personalproject.user_service.models.Account;
import com.personalproject.user_service.models.AccountStatus;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private final Account user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Account user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    public Account getUser() {
        return this.user;
    }
    @Override
    public boolean isAccountNonExpired() {
        return this.user.getStatus().compareTo(AccountStatus.ACTIVE) == 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getStatus().compareTo(AccountStatus.LOCKED) != 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.getStatus().compareTo(AccountStatus.ACTIVE) == 0;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }
}
