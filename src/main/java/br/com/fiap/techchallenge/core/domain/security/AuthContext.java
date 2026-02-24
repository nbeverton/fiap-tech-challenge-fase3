package br.com.fiap.techchallenge.core.domain.security;

import br.com.fiap.techchallenge.core.domain.exception.security.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthContext {

    private AuthContext() {}

    public static String userId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        return auth.getPrincipal().toString();
    }

    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        String expected = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(expected));
    }

    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public static boolean isOwner() {
        return hasRole("OWNER");
    }

    public static boolean isClient() {
        return hasRole("CLIENT");
    }
}