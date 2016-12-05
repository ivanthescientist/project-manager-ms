package com.isc.project.manager.security;

import com.isc.project.manager.persistence.domain.UserType;

public class AuthenticatedUser {
    private Long id;
    private String username;
    private UserType type;
    private String tenantCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public boolean isAdmin() {
        return type.equals(UserType.ADMIN);
    }

    public boolean isManager() {
        return type.equals(UserType.MANAGER);
    }

    public boolean isUser() {
        return type.equals(UserType.USER);
    }
}
