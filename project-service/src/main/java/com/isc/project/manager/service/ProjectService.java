package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.ProjectDTO;
import com.isc.project.manager.persistence.domain.ProjectEntity;
import com.isc.project.manager.persistence.repository.ProjectEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectMapperService mapperService;
    private final ProjectEntityRepository repository;

    @Autowired
    public ProjectService(ProjectMapperService mapperService, ProjectEntityRepository repository) {
        this.mapperService = mapperService;
        this.repository = repository;
    }

    public List<ProjectDTO> findAll() {
        return repository.findAll().stream()
                .map(mapperService::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> findAllByTenantCode(String tenantCode) {
        return repository.findAllByTenantCode(tenantCode).stream()
                .map(mapperService::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProjectDTO> findById(Long id) {
        return repository.findOne(id)
                .map(mapperService::toDTO);
    }

    @Transactional
    public ProjectDTO create(ProjectDTO projectDTO) {
        projectDTO.setId(null);

        ProjectEntity entity = mapperService.toEntity(projectDTO);
        entity = repository.save(entity);

        return mapperService.toDTO(entity);
    }

    @Transactional
    public Optional<ProjectDTO> update(ProjectDTO projectDTO) {
        return repository.findOne(projectDTO.getId())
                .map(entity -> mapperService.fillEntity(projectDTO, entity))
                .map(repository::save)
                .map(mapperService::toDTO);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(id);
    }
}
