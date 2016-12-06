package com.isc.project.manager.persistence.repository;

import com.isc.project.manager.persistence.domain.UserEntity;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserEntityRepository extends Repository<UserEntity, Long>, BaseRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByTenantCode(String tenantCode);
}
