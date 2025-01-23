package org.brapi.test.BrAPITestServer.service;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {
    public static UUID getCurrentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        UUID userId = null;
        if (context.getAuthentication().getPrincipal() != null) {
            userId = UUID.fromString(context.getAuthentication().getPrincipal().toString());
        }
        return userId;
    }
}