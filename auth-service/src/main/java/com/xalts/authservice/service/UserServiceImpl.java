package com.xalts.authservice.service;

import com.xalts.authservice.dto.LoginRequest;
import com.xalts.authservice.model.User;
import com.xalts.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Authenticate User
    @Override
    public boolean authenticateUser(LoginRequest loginRequest) {
        User userLogin = userRepository.findByUsername(loginRequest.getUsername());
        return userLogin != null && passwordEncoder.matches(loginRequest.getPassword(), userLogin.getPassword());
    }
}
