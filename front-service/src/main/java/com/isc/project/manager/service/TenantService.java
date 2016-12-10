package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.TenantDTO;
import com.isc.project.manager.persistence.repository.TenantEntityRepository;
import com.isc.project.manager.security.authorization.SecurityDecoratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantService {
    private final TenantEntityRepository repository;
    private final TenantMapperService mapperService;
    private final SecurityDecoratorService securityDecoratorService;

    @Autowired
    public TenantService(TenantEntityRepository repository,
                         TenantMapperService mapperService,
                         SecurityDecoratorService securityDecoratorService) {
        this.repository = repository;
        this.mapperService = mapperService;
        this.securityDecoratorService = securityDecoratorService;
    }

    public List<TenantDTO> findAll() {
        return repository.findAll().stream()
                .map(mapperService::toDTO)
                .map(securityDecoratorService::executeReadPolicy)
                .collect(Collectors.toList());
    }

    public Optional<TenantDTO> findById(Long id) {
        return repository.findOne(id)
                .map(mapperService::toDTO)
                .map(securityDecoratorService::executeReadPolicy);
    }

    public Optional<TenantDTO> findByCode(String code) {
        return repository.findByCode(code)
                .map(mapperService::toDTO)
                .map(securityDecoratorService::executeReadPolicy);
    }

    @Transactional
    public TenantDTO create(TenantDTO dto) {
        dto.setId(null);
        securityDecoratorService.executeCreatePolicy(dto);
        return mapperService.toDTO(repository.save(mapperService.toEntity(dto)));
    }

    @Transactional
    public Optional<TenantDTO> update(TenantDTO dto) {
        securityDecoratorService.executeUpdatePolicy(findById(dto.getId()).orElse(new TenantDTO()));

        return repository.findOne(dto.getId())
                .map(entity -> mapperService.fillEntity(entity, dto))
                .map(repository::save)
                .map(mapperService::toDTO);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(id);
    }
}
