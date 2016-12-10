package com.isc.project.manager;

import com.isc.project.manager.api.dto.TenantDTO;
import com.isc.project.manager.persistence.repository.TenantEntityRepository;
import com.isc.project.manager.persistence.repository.UserEntityRepository;
import com.isc.project.manager.service.TenantService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TenantServiceTest {

    @Autowired
    private TenantService service;

    @Autowired
    private TenantEntityRepository tenantEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Before
    public void setUp() {
        tenantEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
    }

    @Test
    public void testBasicCRUD() {
        TenantDTO tenant1 = new TenantDTO();
        tenant1.setCode("tenant-1");

        TenantDTO tenant2 = new TenantDTO();
        tenant2.setCode("tenant-2");

        tenant1 = service.create(tenant1);
        tenant2 = service.create(tenant2);

        Assert.assertNotNull(tenant1.getId());
        Assert.assertNotNull(tenant2.getId());

        tenant1.setCode("special-code-1");
        tenant1.setName("Tenant 1");
        tenant1.setDescription("Tenant 1 Description");
        tenant2.setCode("special-code-2");
        tenant2.setName("Tenant 2");
        tenant2.setDescription("Tenant 2 Description");

        tenant1 = service.update(tenant1).orElse(new TenantDTO());
        tenant2 = service.update(tenant2).orElse(new TenantDTO());

        Assert.assertEquals("tenant-1", tenant1.getCode());
        Assert.assertEquals("Tenant 1", tenant1.getName());
        Assert.assertEquals("Tenant 1 Description", tenant1.getDescription());

        Assert.assertEquals("tenant-2", tenant2.getCode());
        Assert.assertEquals("Tenant 2", tenant2.getName());
        Assert.assertEquals("Tenant 2 Description", tenant2.getDescription());

        Assert.assertTrue(service.findByCode("tenant-1").isPresent());
        Assert.assertTrue(service.findByCode("tenant-2").isPresent());
        Assert.assertEquals(2, service.findAll().size());

        service.delete(tenant1.getId());
        Assert.assertFalse(service.findByCode("tenant-1").isPresent());
        Assert.assertEquals(1, service.findAll().size());

        service.delete(tenant2.getId());
        Assert.assertFalse(service.findByCode("tenant-2").isPresent());
        Assert.assertEquals(0, service.findAll().size());
    }
}
