package com.isc.project.manager.persistence.repository;

import com.isc.project.manager.persistence.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Stream<UserEntity> findByTenantCode(String tenantCode);
}
