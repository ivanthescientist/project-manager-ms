package com.isc.project.manager;

import com.isc.project.manager.api.dto.UserDTO;
import com.isc.project.manager.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void testBasicCrud() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user1");
        userDTO.setPassword("password");
        userDTO.setType("MANAGER");
        userDTO.setTenantCode("tenant-1");

        userDTO = service.create(userDTO);
        Assert.assertNotNull(userDTO.getId());
        Assert.assertNull(userDTO.getPassword());

        userDTO.setTenantCode("tenant-2");
        userDTO.setUsername("different-username");
        service.update(userDTO);

        userDTO = service.findByUsername("user1").orElse(new UserDTO());
        Assert.assertEquals("tenant-2", userDTO.getTenantCode());

        service.delete(userDTO.getId());
        Assert.assertFalse(service.findByUsername("user1").isPresent());
    }
}
