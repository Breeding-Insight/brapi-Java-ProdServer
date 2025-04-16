package org.brapi.test.BrAPITestServer.service;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String getCurrentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = "";
        if (context.getAuthentication().getPrincipal() != null) {
            userId = context.getAuthentication().getPrincipal().toString();
        }
        return userId;
    }
}