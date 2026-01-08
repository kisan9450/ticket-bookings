package com.xalts.authservice.service;

import com.xalts.authservice.dto.LoginRequest;

public interface UserService {
    boolean authenticateUser(LoginRequest loginRequest);
}
