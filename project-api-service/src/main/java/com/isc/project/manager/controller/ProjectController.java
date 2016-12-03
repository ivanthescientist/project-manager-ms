package com.isc.project.manager.controller;

import com.isc.project.manager.api.ProjectDTO;
import com.isc.project.manager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService service;

    @Autowired
    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProjectDTO> findAll() {
        return service.findAll();
    }

    @RequestMapping(value = "", method = RequestMethod.GET, params = "tenant")
    public List<ProjectDTO> findAllByTenantCode(@RequestParam String tenant) {
        return service.findAllByTenantCode(tenant);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProjectDTO findById(@PathVariable Long id) {
        return service.findById(id).orElseThrow(() -> new RuntimeException("Project #" + id + " not found."));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProjectDTO create(@RequestBody ProjectDTO projectDTO) {
        return service.create(projectDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ProjectDTO update(@RequestBody ProjectDTO projectDTO, @PathVariable Long id) {
        projectDTO.setId(id);
        return service.update(projectDTO).orElseThrow(() -> new RuntimeException("Project #" + id + " not found."));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
