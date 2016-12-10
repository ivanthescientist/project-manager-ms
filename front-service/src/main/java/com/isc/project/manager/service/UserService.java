package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.UserDTO;
import com.isc.project.manager.persistence.domain.UserType;
import com.isc.project.manager.persistence.repository.UserEntityRepository;
import com.isc.project.manager.security.authorization.AuthorizationService;
import com.isc.project.manager.security.authorization.SecurityActionType;
import com.isc.project.manager.security.authorization.UserHolder;
import com.isc.project.manager.service.mapping.UserMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserEntityRepository repository;
    private final UserMapperService mapperService;
    private final AuthorizationService authorizationService;
    private final UserHolder userHolder;

    @Autowired
    public UserService(UserEntityRepository repository,
                       UserMapperService mapperService,
                       AuthorizationService authorizationService,
                       UserHolder userHolder) {
        this.repository = repository;
        this.mapperService = mapperService;
        this.authorizationService = authorizationService;
        this.userHolder = userHolder;
    }

    public Optional<UserDTO> findById(Long id) {
        return repository.findOne(id)
                .map(mapperService::toDTO)
                .filter(userDTO -> authorizationService.canAccess(userDTO, SecurityActionType.READ));
    }

    public List<UserDTO> findAll() {
        return repository.findAll().stream()
                .map(mapperService::toDTO)
                .filter(userDTO -> authorizationService.canAccess(userDTO, SecurityActionType.READ))
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAllByTenantCode(String tenantCode) {
        return repository.findByTenantCode(tenantCode).stream()
                .map(mapperService::toDTO)
                .filter(userDTO -> authorizationService.canAccess(userDTO, SecurityActionType.READ))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(mapperService::toDTO)
                .filter(userDTO -> authorizationService.canAccess(userDTO, SecurityActionType.READ));
    }

    @Transactional
    public UserDTO create(UserDTO userDTO) {
        if(!userHolder.getCurrentUser().isAdmin()) {
            throw new AccessDeniedException("Access Denied");
        }
        return mapperService.toDTO(repository.save(mapperService.toEntity(userDTO)));
    }

    @Transactional
    public Optional<UserDTO> update(UserDTO userDTO) {
        authorizationService.checkAccess(userDTO, SecurityActionType.UPDATE);

        return repository.findOne(userDTO.getId())
                .map((userEntity -> mapperService.fillEntity(userEntity, userDTO)))
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
