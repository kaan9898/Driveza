package com.team3.driveza.service;

import com.team3.driveza.Dto.Auth.AuthResponseDto;
import com.team3.driveza.Dto.Auth.LoginRequestDto;
import com.team3.driveza.Dto.Auth.RegisterRequestDto;
import com.team3.driveza.Dto.User.UserFormDto;
import com.team3.driveza.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDto register(RegisterRequestDto request) {
        try {
            userService.getUserByEmail(request.getEmail());
            throw new RuntimeException("Email already in use");
        } catch (RuntimeException e) {
        }

        UserFormDto form = new UserFormDto();
        form.setName(request.getName());
        form.setEmail(request.getEmail());
        form.setPassword(request.getPassword());

        User created = userService.createUser(form);
        return toResponse(created, "Registration successful.");
    }

    public AuthResponseDto login(LoginRequestDto request) {
        User user = userService.getUserByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return toResponse(user, "Login successful.");
    }

    private AuthResponseDto toResponse(User user, String message) {
        return AuthResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .message(message)
                .build();
    }
}