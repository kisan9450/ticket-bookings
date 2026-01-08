package com.xalts.userservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;
    private static final String AUTH_SERVICE_URL = "http://localhost:8080/auth/validate-token";

    public JwtAuthFilter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // ✅ Trust API Gateway if it has already verified the token
        String verifiedUser = request.getHeader("X-Verified-User");
        if (verifiedUser != null) {
            System.out.println("✅ API Gateway verified user: " + verifiedUser);
            authenticateUser(verifiedUser);
            filterChain.doFilter(request, response);
            return;
        }

        // Extract Token for Direct Requests
        String token = extractToken(request);
        if (token == null) {
            System.out.println("❌ Missing Authorization Header");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing Authorization Header");
            return;
        }

        // ✅ Validate Token with `auth-service`
        if (!validateTokenWithAuthService(token)) {
            System.out.println("❌ Invalid Token");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid Token");
            return;
        }

        // ✅ Set Authentication in Security Context
        authenticateUser("authenticated-user"); // Assigning a default user identity

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }

    private boolean validateTokenWithAuthService(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Boolean> response = restTemplate.exchange(
                    AUTH_SERVICE_URL,
                    HttpMethod.GET,
                    entity,
                    Boolean.class
            );

            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            return false; // If auth-service fails or token is invalid
        }
    }

    private void authenticateUser(String username) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Assign default role
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
