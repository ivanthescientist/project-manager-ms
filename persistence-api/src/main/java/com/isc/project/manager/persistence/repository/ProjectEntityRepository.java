package com.isc.project.manager.persistence.repository;

import com.isc.project.manager.persistence.domain.ProjectEntity;
import org.springframework.data.repository.Repository;


import java.util.List;

public interface ProjectEntityRepository extends BaseRepository<ProjectEntity, Long>, Repository<ProjectEntity, Long> {
    List<ProjectEntity> findAllByTenantCode(String tenantCode);
}
