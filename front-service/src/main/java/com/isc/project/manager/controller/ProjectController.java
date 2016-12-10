package com.isc.project.manager.controller;

import com.isc.project.manager.api.dto.ProjectDTO;
import com.isc.project.manager.security.authentication.AuthenticatedUser;
import com.isc.project.manager.service.ProjectProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectProxyService proxyService;

    @Autowired
    public ProjectController(ProjectProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProjectDTO> findAll() {
        return proxyService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProjectDTO getById(@PathVariable Long id) {
        return proxyService.getById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProjectDTO create(@RequestBody ProjectDTO projectDTO) {
        return proxyService.create(projectDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ProjectDTO update(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        return proxyService.update(id, projectDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        proxyService.delete(id);
    }
}
