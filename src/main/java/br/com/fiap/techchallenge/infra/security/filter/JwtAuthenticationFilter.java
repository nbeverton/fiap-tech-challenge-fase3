package br.com.fiap.techchallenge.infra.security.filter;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String header = request.getHeader("Authorization");

//        if (header != null
//                && header.startsWith("Bearer ")
//                && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            String token = header.substring(7);
//
//            try {
//                String userId = jwtService.extractUserId(token);
//                String role = jwtService.extractRole(token);
//
//                // ✅ DIAGNÓSTICO
//                System.out.println("JWT DEBUG -> userId = " + userId);
//                System.out.println("JWT DEBUG -> role (raw) = [" + role + "]");
//
//                // ✅ NORMALIZAÇÃO (mata ROLE_ROLE_ e variações de case/espaço)
//                String normalizedRole = role == null ? "" : role.trim().toUpperCase();
//                if (normalizedRole.startsWith("ROLE_")) {
//                    normalizedRole = normalizedRole.substring(5);
//                }
//
//                System.out.println("JWT DEBUG -> role (normalized) = [" + normalizedRole + "]");
//                System.out.println("JWT DEBUG -> " + request.getMethod() + " " + request.getRequestURI());
//
//                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + normalizedRole));
//                System.out.println("JWT DEBUG -> authorities = " + authorities);
//
//                var auth = new UsernamePasswordAuthenticationToken(
//                        userId,
//                        null,
//                        authorities
//                );
//
//                SecurityContextHolder.getContext().setAuthentication(auth);
//
//            } catch (Exception e) {
//                SecurityContextHolder.clearContext();
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null
                && header.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = header.substring(7);

            try {
                String userId = jwtService.extractUserId(token);
                String role = jwtService.extractRole(token);

                var auth = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // Token inválido/expirado/assinatura incorreta:
                // não autentica e deixa seguir.
                // Se a rota exigir auth, o Spring bloqueia; se for permitAll (ex: /auth/login), passa.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}