package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.UserDTO;
import com.isc.project.manager.persistence.domain.UserEntity;
import com.isc.project.manager.persistence.domain.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserMapperService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setTenantCode(entity.getTenantCode());
        dto.setType(entity.getType().name());

        return dto;
    }

    public UserEntity toEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();

        entity = fillEntity(entity, dto);
        entity.setUsername(dto.getUsername());

        return entity;
    }

    public UserEntity fillEntity(UserEntity entity, UserDTO dto) {
        if(dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        entity.setTenantCode(dto.getTenantCode());
        entity.setType(UserType.valueOf(dto.getType()));

        return entity;
    }
}
