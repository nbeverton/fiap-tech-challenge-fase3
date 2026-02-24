package br.com.fiap.techchallenge.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.fiap.techchallenge.infra.security.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        // =====================================================
        // 🔐 MODO SEGURANÇA (JWT ATIVO) - CÓDIGO LUIS
        // Para usar: DESCOMENTE este bloco e COMENTE o "MODO TESTE"
        // =====================================================
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                        .exceptionHandling(ex -> ex
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setStatus(401);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"message\":\"Unauthorized: missing or invalid token\"}");
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setStatus(403);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"message\":\"Forbidden: insufficient permissions\"}");
                                })
                        )
                        .authorizeHttpRequests(auth -> auth

                                // PUBLIC
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()

                                // USERS
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                                // TODO: deixar GET/PUT /users/{id} autenticado e checa "self or admin" no service:
                                .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/users/**").authenticated()

                                // ADDRESSES
                                .requestMatchers(HttpMethod.POST, "/addresses").permitAll()
                                .requestMatchers(HttpMethod.GET, "/addresses").permitAll()
                                .requestMatchers(HttpMethod.GET, "/addresses/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/addresses/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/addresses/**").hasRole("ADMIN")

                                // USER-ADDRESSES
                                .requestMatchers(HttpMethod.GET, "/user-addresses").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/user-addresses").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/user-addresses/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/user-addresses/**").authenticated()

                                // USERS/{userId}/addresses (link)
                                .requestMatchers(HttpMethod.POST, "/users/*/addresses").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/users/*/addresses").authenticated()

                                // RESTAURANTS
                                .requestMatchers(HttpMethod.GET, "/restaurants/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/restaurants").hasAnyRole("OWNER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/restaurants/**").hasAnyRole("OWNER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/restaurants/**").hasRole("ADMIN")

                                // MENUS
                                .requestMatchers(HttpMethod.GET, "/restaurants/*/menus").permitAll()
                                .requestMatchers(HttpMethod.POST, "/restaurants/*/menus").hasAnyRole("OWNER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/restaurants/*/menus/*").hasAnyRole("OWNER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/restaurants/*/menus/*").hasAnyRole("OWNER", "ADMIN")

                                // ORDERS
                                .requestMatchers(HttpMethod.POST, "/orders").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.GET, "/orders/me").hasRole("CLIENT")

                                // GET /orders/{id} -> precisa ser autenticado e a regra "admin OU dono" fica no service
                                .requestMatchers(HttpMethod.GET, "/orders/**").authenticated()

                                // PATCH /orders/**: //TODO
                                // .requestMatchers(HttpMethod.PATCH, "/orders/**").authenticated()

                                // PAYMENTS //TODO
                                .requestMatchers("/payments/**").authenticated()

                                .anyRequest().authenticated()
                        )
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
