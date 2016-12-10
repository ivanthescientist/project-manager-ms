package com.isc.project.manager.security.authorization;

import com.isc.project.manager.security.authentication.AuthenticatedUser;

public interface UserHolder {
    AuthenticatedUser getCurrentUser();

    String getCurrentTenant();
}
