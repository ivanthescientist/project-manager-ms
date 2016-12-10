package com.isc.project.manager.security.authorization;

import com.isc.project.manager.api.dto.SecuredDTO;
import com.isc.project.manager.api.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UserHolder userHolder;

    @Autowired
    public AuthorizationService(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    public void checkAccess(SecuredDTO securedDTO, SecurityActionType actionType) {
        if(!canAccess(securedDTO, actionType)) {
            throw new AccessDeniedException("Action " + actionType.name() + " is not permitted for given entity.");
        }
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
