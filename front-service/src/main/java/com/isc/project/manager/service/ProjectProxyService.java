package com.isc.project.manager.service;

import com.isc.project.manager.api.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectProxyService {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String serverAddress;

    @Autowired
    public ProjectProxyService(@Qualifier("projectApiServiceAddress") String serverAddress) {
        this.serverAddress = serverAddress;
    }


    public List<ProjectDTO> findAll() {
        return Arrays.asList(restTemplate.getForObject(
                serverAddress + "/api/projects",
                ProjectDTO[].class));
    }

    public List<ProjectDTO> findAllByTenant(String tenantCode) {
        return Arrays.asList(restTemplate.getForObject(
                serverAddress + "/api/projects?tenant=" + tenantCode,
                ProjectDTO[].class
        ));
    }

    public ProjectDTO getById(Long id) {
        return restTemplate.getForObject(
                serverAddress + "/api/projects/" + id,
                ProjectDTO.class
        );
    }

    public ProjectDTO create(ProjectDTO projectDTO) {
        return restTemplate.postForObject(
                serverAddress + "/api/projects",
                projectDTO,
                ProjectDTO.class);
    }

    public ProjectDTO update(Long id, ProjectDTO projectDTO) {
        HttpEntity<ProjectDTO> requestEntity = new HttpEntity<>(projectDTO);
        return restTemplate.exchange(
                serverAddress + "/api/projects/" + id,
                HttpMethod.PUT,
                requestEntity,
                ProjectDTO.class
        ).getBody();
    }

    public void delete(Long id) {
        restTemplate.delete(serverAddress + "/api/projects/" + id);
    }
}
