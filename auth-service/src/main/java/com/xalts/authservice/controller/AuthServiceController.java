package com.xalts.authservice.controller;

import com.xalts.authservice.dto.LoginRequest;
import com.xalts.authservice.service.JwtTokenService;
import com.xalts.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthServiceController {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(false); // Return Boolean false instead of a String
        }
        String token = authHeader.substring(7);
        return ResponseEntity.ok(jwtTokenService.validateToken(token)); // Return Boolean directly
    }


    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean isValidUser = userService.authenticateUser(loginRequest);
        if (isValidUser) {
            String token = jwtTokenService.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials.");
        }
    }
}

