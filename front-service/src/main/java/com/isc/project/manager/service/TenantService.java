package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.TenantDTO;
import com.isc.project.manager.persistence.repository.TenantEntityRepository;
import com.isc.project.manager.security.authorization.AuthorizationService;
import com.isc.project.manager.security.authorization.SecurityActionType;
import com.isc.project.manager.security.authorization.UserHolder;
import com.isc.project.manager.service.mapping.TenantMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantService {
    private final TenantEntityRepository repository;
    private final TenantMapperService mapperService;
    private final AuthorizationService authorizationService;
    private final UserHolder userHolder;


    @Autowired
    public TenantService(TenantEntityRepository repository,
                         TenantMapperService mapperService,
                         AuthorizationService authorizationService,
                         UserHolder userHolder) {
        this.repository = repository;
        this.mapperService = mapperService;
        this.authorizationService = authorizationService;
        this.userHolder = userHolder;
    }


    public List<TenantDTO> findAll() {
        return repository.findAll().stream()
                .map(mapperService::toDTO)
                .filter(tenantDTO -> authorizationService.canAccess(tenantDTO, SecurityActionType.READ))
                .collect(Collectors.toList());
    }

    public Optional<TenantDTO> findById(Long id) {
        return repository.findOne(id)
                .map(mapperService::toDTO)
                .filter(tenantDTO -> authorizationService.canAccess(tenantDTO, SecurityActionType.READ));
    }

    public Optional<TenantDTO> findByCode(String code) {
        return repository.findByCode(code)
                .map(mapperService::toDTO)
                .filter(tenantDTO -> authorizationService.canAccess(tenantDTO, SecurityActionType.READ));
    }

    @Transactional
    public TenantDTO create(TenantDTO dto) {
        if(!userHolder.getCurrentUser().isAdmin()) {
            throw new AccessDeniedException("Access Denied");
        }

        return mapperService.toDTO(repository.save(mapperService.toEntity(dto)));
    }

    @Transactional
    public Optional<TenantDTO> update(TenantDTO newDTO) {
        if(!userHolder.getCurrentUser().isAdmin()) {
            throw new AccessDeniedException("Access Denied");
        }

        return repository.findOne(newDTO.getId())
                .map(entity -> mapperService.fillEntity(entity, newDTO))
                .map(repository::save)
                .map(mapperService::toDTO);
    }

    @Transactional
    public void delete(Long id) {
        if(!userHolder.getCurrentUser().isAdmin()) {
            throw new AccessDeniedException("Access Denied");
        }
        repository.delete(id);
    }
}
