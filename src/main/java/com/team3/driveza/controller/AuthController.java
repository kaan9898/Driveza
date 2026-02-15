package com.team3.driveza.controller;

import com.team3.driveza.model.User;
import com.team3.driveza.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles user authentication: registration and login.
 * Public endpoints, no authentication required.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user
     * POST /api/auth/register
     *
     * @param user User object with username, password, etc.
     * @return Created user
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(user));
    }

    /**
     * Login a user
     * POST /api/auth/login
     *
     * @param user User object with username/password
     * @return Logged-in user or token (later)
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return ResponseEntity.ok(
                authService.login(user)
        );
    }
}