package com.team3.driveza.service;

import com.team3.driveza.Dto.User.UserDetailDto;
import com.team3.driveza.Dto.User.UserFormDto;
import com.team3.driveza.model.enums.Role;
import com.team3.driveza.Dto.User.UserListDto;
import com.team3.driveza.model.User;
import com.team3.driveza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserListDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toListDto)
                .collect(Collectors.toList());
    }

    public UserDetailDto getUserById(Long id) {
        return toDetailDto(findOrThrow(id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    @Transactional
    public User createUser(UserFormDto form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        User user = mapFormToEntity(form, new User(), true);
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, UserFormDto form) {
        User user = findOrThrow(id);
        if (!user.getEmail().equals(form.getEmail()) && userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        mapFormToEntity(form, user, false);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(findOrThrow(id));
    }

    private User mapFormToEntity(UserFormDto form, User user, boolean requirePassword) {
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setRole(form.getRole() != null ? form.getRole() : Role.USER);

        if (form.getDob() != null) {
            user.setDob(form.getDob().atStartOfDay(ZoneId.systemDefault()));
        }

        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        } else if (requirePassword) {
            throw new IllegalArgumentException("Password is required.");
        }

        return user;
    }
    public User findOrThrow(Long id) {
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
