package com.isc.project.manager.persistence.repository;

import com.isc.project.manager.persistence.domain.TenantEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantEntityRepository extends CrudRepository<TenantEntity, Long> {
    Optional<TenantEntity> findByCode(String code);
}
