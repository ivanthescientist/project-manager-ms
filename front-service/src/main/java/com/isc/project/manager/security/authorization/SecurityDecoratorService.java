package com.isc.project.manager.security.authorization;

import com.isc.project.manager.api.dto.SecuredDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class SecurityDecoratorService {
    private final AuthorizationService authorizationService;
    private final AuthenticatedUserHolder userHolder;

    @Autowired
    public SecurityDecoratorService(AuthorizationService authorizationService, AuthenticatedUserHolder userHolder) {
        this.authorizationService = authorizationService;
        this.userHolder = userHolder;
    }

    public <T extends SecuredDTO> T executeCreatePolicy(T securedDTO) {
        return executeSecurityPolicy(securedDTO, SecurityActionType.CREATE);
    }

    public <T extends SecuredDTO> T executeReadPolicy(T securedDTO) {
        return executeSecurityPolicy(securedDTO, SecurityActionType.READ);
    }

    public <T extends SecuredDTO> T executeUpdatePolicy(T securedDTO) {
        return executeSecurityPolicy(securedDTO, SecurityActionType.UPDATE);
    }

    public <T extends SecuredDTO> T executeDeletePolicy(T securedDTO) {
        return executeSecurityPolicy(securedDTO, SecurityActionType.DELETE);
    }

    public <T extends SecuredDTO> T executeSecurityPolicy(T securedDTO, SecurityActionType actionType) {
        switch (actionType) {
            case CREATE:
                securedDTO.setTenantCode(userHolder.getCurrentUser().getTenantCode());
                break;
        }

        if(!authorizationService.canAccess(securedDTO, actionType)) {
            throw new AccessDeniedException("Access Denied");
        }

        return securedDTO;
    }
}
