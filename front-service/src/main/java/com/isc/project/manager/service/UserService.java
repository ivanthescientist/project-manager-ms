package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.UserDTO;
import com.isc.project.manager.persistence.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserEntityRepository repository;

    @Autowired
    private UserMapperService mapperService;

    public Optional<UserDTO> findById(Long id) {
        return repository.findOne(id)
                .map(mapperService::toDTO);
    }

    public List<UserDTO> findAll() {
        return repository.findAll().stream()
                .map(mapperService::toDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAllByTenantCode(String tenantCode) {
        return repository.findByTenantCode(tenantCode).stream()
                .map(mapperService::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(mapperService::toDTO);
    }

    public UserDTO create(UserDTO userDTO) {
        return mapperService.toDTO(repository.save(mapperService.toEntity(userDTO)));
    }

    public Optional<UserDTO> update(UserDTO userDTO) {
        return repository.findOne(userDTO.getId())
                .map((userEntity -> mapperService.fillEntity(userEntity, userDTO)))
                .map(repository::save)
                .map(mapperService::toDTO);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
