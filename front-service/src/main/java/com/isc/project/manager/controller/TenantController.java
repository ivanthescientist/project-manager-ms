package com.isc.project.manager.controller;

import com.isc.project.manager.api.dto.TenantDTO;
import com.isc.project.manager.api.exception.EntityNotFoundException;
import com.isc.project.manager.security.authentication.AuthenticatedUser;
import com.isc.project.manager.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {
    private final TenantService service;

    @Autowired
    public TenantController(TenantService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TenantDTO> findAll() {
        return service.findAll();
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public TenantDTO getCurrent(@AuthenticationPrincipal AuthenticatedUser user) {
        return service.findByCode(user.getTenantCode())
                .orElseThrow(() -> new EntityNotFoundException("Tenant", user.getTenantCode()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TenantDTO getById(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant", id.toString()));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public TenantDTO create(@RequestBody TenantDTO tenantDTO) {
        return service.create(tenantDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TenantDTO update(@RequestBody TenantDTO tenantDTO, @PathVariable Long id) {
        tenantDTO.setId(id);
        return service.update(tenantDTO).orElseThrow(() -> new EntityNotFoundException("Tenant", id.toString()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
