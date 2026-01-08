package com.xalts.userservice.service;

import com.xalts.userservice.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long userId);
    User createUser(User user);
    void deleteUser(Long userId);
}
