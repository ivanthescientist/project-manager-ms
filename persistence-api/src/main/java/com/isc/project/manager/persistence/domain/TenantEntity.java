package com.isc.project.manager.persistence.domain;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "tenants")
public class TenantEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
