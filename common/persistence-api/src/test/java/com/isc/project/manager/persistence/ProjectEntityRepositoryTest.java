package com.isc.project.manager.persistence;

import com.isc.project.manager.persistence.configuration.PersistenceConfiguration;
import com.isc.project.manager.persistence.domain.ProjectEntity;
import com.isc.project.manager.persistence.repository.ProjectEntityRepository;
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
public class ProjectEntityRepositoryTest {

    @Autowired
    private ProjectEntityRepository repository;

    @Test
    public void testTenantQuery() {
        ProjectEntity project1 = repository.save(createProject("project1", "tenant-1"));
        ProjectEntity project2 = repository.save(createProject("project2", "tenant-1"));
        ProjectEntity project3 = repository.save(createProject("project3", "tenant-2"));
        ProjectEntity project4 = repository.save(createProject("project4", "tenant-2"));
        ProjectEntity project5 = repository.save(createProject("project5", "tenant-3"));

        Assert.assertNotNull(project1.getId());
        Assert.assertEquals(5, repository.findAll().size());
        Assert.assertEquals(2, repository.findAllByTenantCode("tenant-1").size());
        Assert.assertEquals(2, repository.findAllByTenantCode("tenant-2").size());
        Assert.assertEquals(1, repository.findAllByTenantCode("tenant-3").size());
    }

    private ProjectEntity createProject(String name, String tenant) {
        ProjectEntity entity = new ProjectEntity();
        entity.setName(name);
        entity.setDescription(name + "-description");
        entity.setTenantCode(tenant);

        return entity;
    }
}
