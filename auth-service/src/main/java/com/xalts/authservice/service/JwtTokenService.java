package com.xalts.authservice.service;

public interface JwtTokenService {
    String generateToken(String username);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
}
