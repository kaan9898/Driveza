package com.team3.driveza.controller;

import com.team3.driveza.model.User;
import com.team3.driveza.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Serves the custom login page and keeps API endpoints for registration/login.
 * Public endpoints, no authentication required.
 */
@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Renders the Thymeleaf login template expected by Spring Security.
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerForm(@ModelAttribute User user) {
        authService.register(user);
        return "redirect:/login?registered=true";
    }

    /**
     * Register a new user
     * POST /api/auth/register
     *
     * @param user Users object with username, password, etc.
     * @return Created user
     */
    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(user));
    }

    /**
     * Login a user
     * POST /api/auth/login
     *
     * @param user Users object with username/password
     * @return Login response (for now string, later JWT token)
     */
    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user) {
        return ResponseEntity.ok(
                authService.login(user)
        );
    }
}
