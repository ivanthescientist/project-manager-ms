package com.isc.project.manager.service;

import com.isc.project.manager.api.dto.ProjectDTO;
import com.isc.project.manager.persistence.domain.UserType;
import com.isc.project.manager.security.authorization.AuthenticatedUserHolder;
import com.isc.project.manager.security.authorization.SecurityActionType;
import com.isc.project.manager.security.authorization.SecurityDecoratorService;
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
    private final SecurityDecoratorService securityDecoratorService;
    private final AuthenticatedUserHolder userHolder;

    @Autowired
    public ProjectProxyService(@Qualifier("projectApiServiceAddress") String serverAddress,
                               SecurityDecoratorService securityDecoratorService,
                               AuthenticatedUserHolder userHolder) {
        this.serverAddress = serverAddress;
        this.securityDecoratorService = securityDecoratorService;
        this.userHolder = userHolder;
    }


    public List<ProjectDTO> findAll() {
        String tenantCode = userHolder.getCurrentUser().getType() == UserType.ADMIN
                ? ""
                : userHolder.getCurrentUser().getTenantCode();

        return findAllByTenant(tenantCode).stream()
                .map(securityDecoratorService::executeReadPolicy)
                .collect(Collectors.toList());
    }

    private List<ProjectDTO> findAllByTenant(String tenantCode) {
        return Arrays.asList(restTemplate.getForObject(
                serverAddress + "/api/projects" + (tenantCode.isEmpty() ? "" : "?tenant=" + tenantCode),
                ProjectDTO[].class
        ));
    }

    public ProjectDTO getById(Long id) {
        ProjectDTO projectDTO = restTemplate.getForObject(serverAddress + "/api/projects/" + id, ProjectDTO.class);

        return securityDecoratorService.executeReadPolicy(projectDTO);
    }

    public ProjectDTO create(ProjectDTO projectDTO) {
        projectDTO.setId(null);
        securityDecoratorService.executeCreatePolicy(projectDTO);

        return restTemplate.postForObject(
                serverAddress + "/api/projects",
                projectDTO,
                ProjectDTO.class);
    }

    public ProjectDTO update(Long id, ProjectDTO projectDTO) {
        securityDecoratorService.executeUpdatePolicy(getById(id));

        HttpEntity<ProjectDTO> requestEntity = new HttpEntity<>(projectDTO);
        return restTemplate.exchange(
                serverAddress + "/api/projects/" + id,
                HttpMethod.PUT,
                requestEntity,
                ProjectDTO.class
        ).getBody();
    }

    public void delete(Long id) {
        securityDecoratorService.executeDeletePolicy(getById(id));

        restTemplate.delete(serverAddress + "/api/projects/" + id);
    }
}
