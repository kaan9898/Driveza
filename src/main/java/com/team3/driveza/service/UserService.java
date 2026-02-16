package com.team3.driveza.service;

import com.team3.driveza.model.User;
import com.team3.driveza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return new ArrayList<>();
    }

    public User getUserById(Long id) {
        return new User();
    }

    public User createUser(User user) {
        return user;
    }

    public User updateUser(Long id, User user) {
        return user;
    }

    public void deleteUser(Long id) {
        // no-op for now
    }
}