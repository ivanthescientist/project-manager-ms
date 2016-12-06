package com.isc.project.manager.persistence;

import com.isc.project.manager.persistence.configuration.PersistenceConfiguration;
import com.isc.project.manager.persistence.domain.TenantEntity;
import com.isc.project.manager.persistence.repository.TenantEntityRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersistenceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TenantEntityRepositoryTest {

    @Autowired
    private TenantEntityRepository repository;

    @Test
    public void testCRUD() {
        TenantEntity tenant1 = new TenantEntity();
        tenant1.setCode("tenant-1");
        tenant1 = repository.save(tenant1);
        Assert.assertNotNull(tenant1.getId());

        TenantEntity tenant2 = new TenantEntity();
        tenant2.setCode("tenant-2");
        tenant2 = repository.save(tenant2);
        Assert.assertNotNull(tenant2.getId());

        Assert.assertTrue(repository.findByCode("tenant-1").isPresent());
        Assert.assertTrue(repository.findByCode("tenant-2").isPresent());
        Assert.assertEquals(2, repository.findAll().size());

        repository.delete(tenant1);
        Assert.assertFalse(repository.findByCode("tenant-1").isPresent());

        repository.delete(tenant2.getId());
        Assert.assertFalse(repository.findByCode("tenant-2").isPresent());
    }
}
