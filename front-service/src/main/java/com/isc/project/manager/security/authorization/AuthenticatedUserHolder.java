package com.isc.project.manager.security.authorization;

import com.isc.project.manager.security.authentication.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserHolder implements UserHolder {

    @Override
    public AuthenticatedUser getCurrentUser() {
        return (AuthenticatedUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Override
    public String getCurrentTenant() {
        return getCurrentUser().getTenantCode();
    }
}
