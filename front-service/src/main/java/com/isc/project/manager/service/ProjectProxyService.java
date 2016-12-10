package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.ProjectDTO;
import com.isc.project.manager.persistence.domain.UserType;
import com.isc.project.manager.security.authorization.AuthorizationService;
import com.isc.project.manager.security.authorization.SecurityActionType;
import com.isc.project.manager.security.authorization.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectProxyService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String serverAddress;
    private final AuthorizationService authorizationService;
    private final UserHolder userHolder;

    @Autowired
    public ProjectProxyService(@Qualifier("projectApiServiceAddress") String serverAddress,
                               AuthorizationService authorizationService,
                               UserHolder userHolder) {
        this.serverAddress = serverAddress;
        this.authorizationService = authorizationService;
        this.userHolder = userHolder;
    }


    public List<ProjectDTO> findAll() {
        String tenantCode = userHolder.getCurrentUser().getType() == UserType.ADMIN
                ? ""
                : userHolder.getCurrentUser().getTenantCode();

        return findAllByTenant(tenantCode).stream()
                .filter(projectDTO -> authorizationService.canAccess(projectDTO, SecurityActionType.READ))
                .collect(Collectors.toList());
    }

    private List<ProjectDTO> findAllByTenant(String tenantCode) {
        return Arrays.stream(restTemplate.getForObject(
                serverAddress + "/api/projects" + (tenantCode.isEmpty() ? "" : "?tenant=" + tenantCode),
                ProjectDTO[].class))
                .filter(projectDTO -> authorizationService.canAccess(projectDTO, SecurityActionType.READ))
                .collect(Collectors.toList());
    }

    public ProjectDTO getById(Long id) {
        ProjectDTO projectDTO = restTemplate.getForObject(serverAddress + "/api/projects/" + id, ProjectDTO.class);
        authorizationService.checkAccess(projectDTO, SecurityActionType.READ);

        return projectDTO;
    }

    public ProjectDTO create(ProjectDTO projectDTO) {
        projectDTO.setId(null);
        projectDTO.setTenantCode(userHolder.getCurrentTenant());
        authorizationService.checkAccess(projectDTO, SecurityActionType.CREATE);

        return restTemplate.postForObject(
                serverAddress + "/api/projects",
                projectDTO,
                ProjectDTO.class);
    }

    public ProjectDTO update(ProjectDTO newDTO) {
        ProjectDTO oldDTO = getById(newDTO.getId());
        authorizationService.checkAccess(oldDTO, SecurityActionType.UPDATE);

        newDTO.setId(oldDTO.getId());
        newDTO.setTenantCode(oldDTO.getTenantCode());

        HttpEntity<ProjectDTO> requestEntity = new HttpEntity<>(newDTO);
        return restTemplate.exchange(
                serverAddress + "/api/projects/" + newDTO.getId(),
                HttpMethod.PUT,
                requestEntity,
                ProjectDTO.class
        ).getBody();
    }

    public void delete(Long id) {
        authorizationService.checkAccess(getById(id), SecurityActionType.DELETE);

        restTemplate.delete(serverAddress + "/api/projects/" + id);
    }
}
