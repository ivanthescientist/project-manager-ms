package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.ProjectDTO;
import com.isc.project.manager.persistence.domain.ProjectEntity;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapperService {
    public ProjectDTO toDTO(ProjectEntity entity) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setTenantCode(entity.getTenantCode());

        return dto;
    }

    public ProjectEntity toEntity(ProjectDTO dto) {
        ProjectEntity entity = new ProjectEntity();
        fillEntity(dto, entity);

        return entity;
    }

    public ProjectEntity fillEntity(ProjectDTO dto, ProjectEntity entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setTenantCode(dto.getTenantCode());

        return entity;
    }
}
