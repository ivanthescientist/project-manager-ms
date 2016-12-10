package com.isc.project.manager.configuration;

import com.isc.project.manager.persistence.domain.UserType;
import com.isc.project.manager.security.authentication.AuthenticatedUser;
import com.isc.project.manager.security.authorization.UserHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestSecurityConfiguration {

    @Bean
    public UserHolder userHolder() {
        return new UserHolder() {
            @Override
            public AuthenticatedUser getCurrentUser() {
                AuthenticatedUser user = new AuthenticatedUser();
                user.setId(1L);
                user.setTenantCode("tenant-1");
                user.setUsername("admin");
                user.setType(UserType.ADMIN);

                return user;
            }

            @Override
            public String getCurrentTenant() {
                return "tenant-1";
            }
        };
    }
}
