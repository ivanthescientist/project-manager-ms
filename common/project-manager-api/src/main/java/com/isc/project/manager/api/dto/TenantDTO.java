package com.isc.project.manager.api.dto;

public class TenantDTO implements SecuredDTO {
    private Long id;
    private String code;
    private String name;
    private String description;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getTenantCode() {
        return code;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.code = tenantCode;
    }
}
