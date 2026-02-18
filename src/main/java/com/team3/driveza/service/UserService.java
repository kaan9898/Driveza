package com.team3.driveza.service;

import com.team3.driveza.model.User;
import com.team3.driveza.model.enums.Role;
import com.team3.driveza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // TODO: Use DTOs
    public User getUserById(long id) {
        return findOrThrow(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(User newUser) {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        User user = new User();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setDob(newUser.getDob());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));  // hash

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(long id, User newUser) {
        User user = findOrThrow(id);

        if (!user.getEmail().equals(newUser.getEmail())
                && userRepository.existsByEmail(newUser.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setDob(newUser.getDob());
        user.setRole(newUser.getRole());

        // Update password if it has been set
        if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.delete(findOrThrow(id));
    }

    public User findOrThrow(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found. id=" + id));
    }
}