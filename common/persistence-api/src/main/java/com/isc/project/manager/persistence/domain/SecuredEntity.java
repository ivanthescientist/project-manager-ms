package com.isc.project.manager.persistence.domain;

public interface SecuredEntity {
    String getTenantCode();
    void setTenantCode(String tenantCode);
}
