package com.meme.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Utility to get current user ID from JWT token in Spring Security context.
 */
public class UserContextUtil {
    
    /**
     * Gets the current user ID from the JWT in SecurityContext.
     * Can be called from anywhere in the application.
     * 
     * @return the user ID from JWT or null if not available
     */
    public static Long getUserId() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof Jwt) {
                return ((Jwt) principal).getClaim("id");
            }
        } catch (Exception e) {
            // JWT not available or doesn't contain id
        }
        return null;
    }
}
