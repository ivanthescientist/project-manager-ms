package com.isc.project.manager.api.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, String entityId) {
        super("Not found: " + entityName + ":" + entityId);
    }
}
