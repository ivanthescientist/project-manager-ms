package com.isc.project.manager.api.dto;

public interface SecuredDTO {
    public Long getId();
    public void setId(Long id);
    public String getTenantCode();
    public void setTenantCode(String tenantCode);
}
