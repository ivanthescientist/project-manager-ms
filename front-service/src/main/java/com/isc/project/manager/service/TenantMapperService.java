package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.TenantDTO;
import com.isc.project.manager.persistence.domain.TenantEntity;
import org.springframework.stereotype.Service;

@Service
public class TenantMapperService {
    public TenantEntity toEntity(TenantDTO dto) {
        TenantEntity entity = new TenantEntity();
        fillEntity(entity, dto);
        entity.setCode(dto.getCode());

        return entity;
    }

    public TenantDTO toDTO(TenantEntity entity) {
        TenantDTO dto = new TenantDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        return dto;
    }

    public TenantEntity fillEntity(TenantEntity entity, TenantDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
