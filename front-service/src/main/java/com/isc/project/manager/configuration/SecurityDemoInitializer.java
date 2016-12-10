package com.isc.project.manager.configuration;

import com.isc.project.manager.persistence.domain.TenantEntity;
import com.isc.project.manager.persistence.domain.UserEntity;
import com.isc.project.manager.persistence.domain.UserType;
import com.isc.project.manager.persistence.repository.TenantEntityRepository;
import com.isc.project.manager.persistence.repository.UserEntityRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityDemoInitializer implements InitializingBean {
    private final TenantEntityRepository tenantEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityDemoInitializer(TenantEntityRepository tenantEntityRepository, UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.tenantEntityRepository = tenantEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TenantEntity demoTenant = new TenantEntity();
        demoTenant.setCode("DEMO");
        demoTenant.setName("Demo Tenant");
        demoTenant.setDescription("Tenant for demo.");
        tenantEntityRepository.save(demoTenant);

        UserEntity adminEntity = new UserEntity();
        adminEntity.setTenantCode("");
        adminEntity.setType(UserType.ADMIN);
        adminEntity.setUsername("admin");
        adminEntity.setPassword(passwordEncoder.encode("admin"));
        userEntityRepository.save(adminEntity);

        UserEntity managerEntity = new UserEntity();
        managerEntity.setTenantCode("DEMO");
        managerEntity.setType(UserType.MANAGER);
        managerEntity.setUsername("demo-manager");
        managerEntity.setPassword(passwordEncoder.encode("demo-manager"));
        userEntityRepository.save(managerEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setTenantCode("DEMO");
        userEntity.setType(UserType.USER);
        userEntity.setUsername("demo-user");
        userEntity.setPassword("demo-user");
        userEntityRepository.save(userEntity);
    }
}
