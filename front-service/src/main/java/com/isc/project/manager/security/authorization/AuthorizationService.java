package com.isc.project.manager.security.authorization;

import com.isc.project.manager.api.dto.SecuredDTO;
import com.isc.project.manager.api.dto.UserDTO;
import com.isc.project.manager.persistence.domain.SecuredEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthenticatedUserHolder userHolder;

    @Autowired
    public AuthorizationService(AuthenticatedUserHolder userHolder) {
        this.userHolder = userHolder;
    }


    public boolean canAccess(SecuredDTO securedDTO, SecurityActionType actionType) {
        switch (userHolder.getCurrentUser().getType()) {
            case ADMIN:
                return true;
            case USER:
                // TODO: replace with proper ownership check
                if(securedDTO instanceof UserDTO) {
                    return ((UserDTO) securedDTO).getId().equals(userHolder.getCurrentUser().getId());
                }
            case MANAGER:
                return securedDTO.getTenantCode().equals(userHolder.getCurrentTenant());
        }
        return false;
    }
}
