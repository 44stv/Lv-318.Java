package org.uatransport.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, MANAGER, UNACTIVATED;

    @Override
    public String getAuthority() {
        return name();
    }
}
