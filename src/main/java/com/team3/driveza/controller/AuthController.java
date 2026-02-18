package com.team3.driveza.controller;

import com.team3.driveza.Dto.Auth.AuthResponseDto;
import com.team3.driveza.Dto.Auth.LoginRequestDto;
import com.team3.driveza.Dto.Auth.RegisterRequestDto;
import com.team3.driveza.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Serves the custom login/register pages and keeps API endpoints for authentication.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Renders the Thymeleaf login template expected by Spring Security.
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequestDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerForm(@Valid @ModelAttribute("registerRequest") RegisterRequestDto registerRequest,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            authService.register(registerRequest);
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
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
