package com.canyoncompanion.canyon_api.security;

import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Slf4j
@NullMarked
@Builder
@AllArgsConstructor
public class UserDetailImpl implements UserDetails {

    private final UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getRoles()
                .stream()
                .map(role ->
                        new SimpleGrantedAuthority(
                                "ROLE_" + role.getRole().name()
                        )
                )
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Spring Security uses this value
     * as the principal identifier.     *
     * In this project we authenticate using email.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
