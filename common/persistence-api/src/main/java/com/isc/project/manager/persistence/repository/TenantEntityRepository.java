package com.isc.project.manager.persistence.repository;

import com.isc.project.manager.persistence.domain.TenantEntity;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface TenantEntityRepository extends Repository<TenantEntity, Long>, BaseRepository<TenantEntity, Long> {
    Optional<TenantEntity> findByCode(String code);
}
