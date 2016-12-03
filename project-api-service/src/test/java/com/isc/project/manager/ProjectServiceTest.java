package com.isc.project.manager;

import com.isc.project.manager.api.ProjectDTO;
import com.isc.project.manager.service.ProjectService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectServiceTest {

    @Autowired
    private ProjectService service;

    @Test
    public void testBasicCRUD() {
        ProjectDTO dto1 = createProject("tenant-1");
        ProjectDTO dto2 = createProject("tenant-1");
        ProjectDTO dto3 = createProject("tenant-2");

        dto1 = service.create(dto1);
        dto2 = service.create(dto2);
        dto3 = service.create(dto3);

        Assert.assertNotNull(dto1.getId());
        Assert.assertNotNull(dto2.getId());
        Assert.assertNotNull(dto3.getId());

        Assert.assertEquals(3, service.findAll().size());
        Assert.assertEquals(2, service.findAllByTenantCode("tenant-1").size());
        Assert.assertEquals(1, service.findAllByTenantCode("tenant-2").size());

        Assert.assertTrue(service.findById(dto1.getId()).isPresent());

        dto1.setName("special-name");
        dto1.setDescription("special-description");
        dto1.setTenantCode("special-tenant");

        dto1 = service.update(dto1).orElse(new ProjectDTO());
        Assert.assertEquals("special-name", dto1.getName());
        Assert.assertEquals("special-description", dto1.getDescription());
        Assert.assertEquals("special-tenant", dto1.getTenantCode());
        Assert.assertEquals(1, service.findAllByTenantCode("tenant-1").size());
        Assert.assertEquals(1, service.findAllByTenantCode("special-tenant").size());

        service.delete(dto2.getId());
        Assert.assertEquals(0, service.findAllByTenantCode("tenant-1").size());
    }

    private ProjectDTO createProject(String tenant) {
        ProjectDTO dto = new ProjectDTO();
        dto.setName("test-name");
        dto.setDescription("test-description");
        dto.setTenantCode(tenant);

        return dto;
    }
}
