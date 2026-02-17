package com.team3.driveza.service;

import com.team3.driveza.model.User;
import com.team3.driveza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(long id) {
        return findOrThrow(id);
    }

    public Iterable<User> getAllUsers() {
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
        user.setRole(newUser.getRole());
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

        // Şifre gönderildiyse güncelle, gönderilmediyse dokunma
        if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.delete(findOrThrow(id));
    }

    private User findOrThrow(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found. id=" + id));
    }
}