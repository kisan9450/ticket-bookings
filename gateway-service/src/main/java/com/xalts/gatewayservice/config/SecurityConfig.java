package com.xalts.gatewayservice.config;

import com.xalts.gatewayservice.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for APIs
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**").permitAll()                     // Auth Service Endpoints
                        .pathMatchers("/users/create_user/**").permitAll()        // Allow user creation
                        .pathMatchers("/eureka/**").permitAll()                   // Eureka registration
                        .pathMatchers("/actuator/**").permitAll()                 // Actuator endpoints
                        .pathMatchers("/swagger-ui.html", "/v3/api-docs/**").permitAll() // Swagger UI & Docs
                        .pathMatchers("/webjars/**", "/swagger-resources/**").permitAll() // Swagger dependencies
                        .anyExchange().authenticated()  // Secure all other endpoints
                )
                .build();
    }

    @Bean
    public WebFilter jwtAuthFilter() {
        return (exchange, chain) -> {
            String token = extractToken(exchange);
            if (token == null) {
                return chain.filter(exchange); // No token, proceed without authentication
            }

            try {
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);

                    // Create authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, null);

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
                }
            } catch (Exception e) {
                return Mono.error(new RuntimeException("Invalid token"));
            }
            return Mono.error(new RuntimeException("Unauthorized"));
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
