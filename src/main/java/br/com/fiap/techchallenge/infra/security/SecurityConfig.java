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
                                                        response.getWriter().write(
                                                                        "{\"message\":\"Client não possui token para criar o pedido\"}");
                                                })
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        response.setStatus(403);
                                                        response.setContentType("application/json");
                                                        response.getWriter().write(
                                                                        "{\"message\":\"Client não possui permissão para criar o pedido\"}");
                                                }))
                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/addresses").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/restaurants").permitAll()

                                                // ============================
                                                // 🔐 SOMENTE CLIENT pode criar pedido com autenticação
                                                // ============================
                                                .requestMatchers(HttpMethod.POST, "/orders")
                                                .hasRole("CLIENT")

                                                // (se quiser liberar outras operações de order, deixe só para elas)
                                                .requestMatchers(HttpMethod.GET, "/orders/**").permitAll()
                                                .requestMatchers(HttpMethod.PATCH, "/orders/**").permitAll()

                                                // PAYMENT continua livre (se existir no seu projeto)
                                                .requestMatchers("/payments/**").permitAll()

                                                // MENU
                                                .requestMatchers(HttpMethod.POST, "/restaurants/*/menus").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/restaurants/*/menus").permitAll()
                                                .requestMatchers(HttpMethod.PUT, "/restaurants/*/menus/*").permitAll()
                                                .requestMatchers(HttpMethod.DELETE, "/restaurants/*/menus/*")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**")
                                                .permitAll()

                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // =====================================================
        // 🔓 MODO TESTE (LIBERA TUDO)
        // Para usar: DESCOMENTE este bloco e COMENTE o "MODO SEGURANÇA"
        // =====================================================
        /*
         * @Bean
         * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         * 
         * http
         * .csrf(csrf -> csrf.disable())
         * .sessionManagement(session ->
         * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         * )
         * .authorizeHttpRequests(auth -> auth
         * .anyRequest().permitAll()
         * );
         * 
         * return http.build();
         * }
         */
}
