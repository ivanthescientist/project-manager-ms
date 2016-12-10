package com.isc.project.manager.security.authorization;

import com.isc.project.manager.security.authentication.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserHolder {

    public AuthenticatedUser getCurrentUser() {
        return (AuthenticatedUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public String getCurrentTenant() {
        return getCurrentUser().getTenantCode();
    }
}
