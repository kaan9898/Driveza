package com.team3.driveza.service;

import com.team3.driveza.Dto.User.UserDetailDto;
import com.team3.driveza.Dto.User.UserFormDto;
import com.team3.driveza.Dto.User.UserListDto;
import com.team3.driveza.exception.ConflictException;
import com.team3.driveza.exception.ResourceNotFoundException;
import com.team3.driveza.model.User;
import com.team3.driveza.model.enums.Role;
import com.team3.driveza.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserListDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toListDto);
    }

    public UserDetailDto getUserById(Long id) {
        return toDetailDto(findOrThrow(id));
    }

    public User getUserEntityByEmail(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public UserDetailDto getUserByEmail(String email) throws ResourceNotFoundException {
        return toDetailDto(getUserEntityByEmail(email));
    }

    public User getAvailableUserEntityByEmail(String email) throws ResourceNotFoundException {
        return userRepository.findByEmailAndDisabledFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public User createUser(UserFormDto form) throws ConflictException {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new ConflictException("Email already in use.");
        }
        User user = mapFormToEntity(form, new User(), true);
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, UserFormDto form) throws ConflictException {
        User user = findOrThrow(id);
        if (!user.getEmail().equals(form.getEmail()) && userRepository.existsByEmail(form.getEmail())) {
            throw new ConflictException("Email already in use.");
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
            user.setDob(form.getDob().atStartOfDay(ZoneOffset.UTC));
        }

        if (requirePassword) {
            if (form.getPassword() != null && !form.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(form.getPassword()));
            } else {
                throw new IllegalArgumentException("Password is required.");
            }
        }

        return user;
    }

    public User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. id=" + id));
    }

    private UserListDto toListDto(User user) {
        return UserListDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .dob(toLocalDate(user.getDob()))
                .disabled(user.getDisabled())
                .build();
    }

    private UserDetailDto toDetailDto(User user) {
        return UserDetailDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .dob(toLocalDate(user.getDob()))
                .disabled(user.getDisabled())
                .build();
    }

    private LocalDate toLocalDate(java.time.ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toLocalDate() : null;
    }

    //password change method
    public void changePassword(String email, String oldPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

//        old password checking
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is wrong");
        }
        //new password rules
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("Password must be 6+ characters");
        }

        //password confirm
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Password not match");
        }

        //save new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);


    }

    @Transactional
    public void updateProfile(String email, String name, String dob) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(name.trim());

        if (dob != null && !dob.isBlank()) {
            LocalDate ld = LocalDate.parse(dob);
            ZonedDateTime zdt = ld.atStartOfDay(ZoneId.systemDefault());

            user.setDob(zdt);
        } else {
            user.setDob(null);
        }
        userRepository.save(user);
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public long getUserCountByRole(Role role) {
        return userRepository.countByRole(role);
    }
}
