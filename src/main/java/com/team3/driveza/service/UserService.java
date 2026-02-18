package com.team3.driveza.service;

import com.team3.driveza.Dto.User.UserDetailDto;
import com.team3.driveza.Dto.User.UserFormDto;
import com.team3.driveza.model.enums.Role;
import com.team3.driveza.Dto.User.UserListDto;
import com.team3.driveza.model.User;
import com.team3.driveza.model.enums.Role;
import com.team3.driveza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
=======
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserListDto> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(this::toListDto)
                .collect(Collectors.toList());
    }

    public UserDetailDto getUserById(long id) {
        return toDetailDto(findOrThrow(id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    public User getUserById(long id) {
        return findOrThrow(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(UserFormDto form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        User user = mapFormToEntity(form, new User(), true);
=======

        User user = new User();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setDob(newUser.getDob());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));  // hash

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(long id, UserFormDto form) {
        User user = findOrThrow(id);
        if (!user.getEmail().equals(form.getEmail()) && userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        mapFormToEntity(form, user, false);
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

    private User mapFormToEntity(UserFormDto form, User user, boolean requirePassword) {
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setRole(form.getRole() != null ? form.getRole() : Role.USER);
        LocalDate dob = form.getDob();
        if (dob != null) {
            user.setDob(dob.atStartOfDay(ZoneId.systemDefault()));
        }

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        } else if (requirePassword && (user.getPassword() == null || user.getPassword().isBlank())) {
            throw new IllegalArgumentException("Password is required.");
        }

        return user;
    }

    private User findOrThrow(long id) {

    public User findOrThrow(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found. id=" + id));
    }

    private UserListDto toListDto(User user) {
        return UserListDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .dob(toLocalDate(user.getDob()))
                .build();
    }

    private UserDetailDto toDetailDto(User user) {
        return UserDetailDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .dob(toLocalDate(user.getDob()))
                .createdAt(user.getDob())
                .build();
    }

    private LocalDate toLocalDate(java.time.ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toLocalDate() : null;
    }
}
