package com.team3.driveza.controller;

import com.team3.driveza.model.Users;
import com.team3.driveza.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User account management endpoints.
 * Authenticated users only.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users (admin only, optional)
     * GET /api/users
     *
     * @return List of all users
     */
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get user by ID
     * GET /api/users/{id}
     *
     * @param id User ID
     * @return Users object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Register a new user
     * POST /api/users
     *
     * @param user Users object (username, password, etc.)
     * @return Created user
     */
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    /**
     * Update user account info
     * PUT /api/users/{id}
     *
     * @param id User ID
     * @param user Updated user info
     * @return Updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(
            @PathVariable Long id,
            @RequestBody Users user) {

        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Delete a user account
     * DELETE /api/users/{id}
     *
     * @param id User ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}