package com.team3.driveza.service;

import com.team3.driveza.model.User;
import com.team3.driveza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // TODO: Use DTOs
    public User getUserById(long id) throws RuntimeException {
        return getUser(id);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(long id) throws RuntimeException {
        User user = getUser(id);
        userRepository.delete(user);
    }

    public User createUser(User newUser) {
        User user = new User();
        user.setName(newUser.getName());
        user.setPassword(newUser.getPassword());
        user.setEmail(newUser.getEmail());
        user.setDob(newUser.getDob());
        user.setRole(newUser.getRole());
        return user;
    }

    public User updateUser(long id, User newUser) throws RuntimeException {
        User user = getUser(id);
        user.setName(newUser.getName());
        user.setPassword(newUser.getPassword());
        user.setEmail(newUser.getEmail());
        user.setDob(newUser.getDob());
        user.setRole(newUser.getRole());
        return user;
    }

    private User getUser(long id) throws RuntimeException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        return optionalUser.get();
    }
}
