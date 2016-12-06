package com.isc.project.manager.persistence;

import com.isc.project.manager.persistence.configuration.PersistenceConfiguration;
import com.isc.project.manager.persistence.domain.UserEntity;
import com.isc.project.manager.persistence.domain.UserType;
import com.isc.project.manager.persistence.repository.UserEntityRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersistenceConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserEntityRepositoryTest {

    @Autowired
    private UserEntityRepository repository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCRUD() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");
        userEntity.setType(UserType.ADMIN);
        userEntity.setPassword("password");
        userEntity = repository.save(userEntity);

        Assert.assertNotNull(userEntity.getId());
        Assert.assertTrue(repository.findByUsername("user1").isPresent());

        UserEntity duplicate = new UserEntity();
        duplicate.setUsername("user1");
        expectedException.expect(DataIntegrityViolationException.class);
        duplicate = repository.save(duplicate);

        userEntity.setTenantCode("tenant-1");
        userEntity = repository.save(userEntity);

        Assert.assertEquals(1, repository.findByTenantCode("tenant-1").size());

        repository.delete(userEntity.getId());
        Assert.assertFalse(repository.findByUsername("user1").isPresent());
    }
}
