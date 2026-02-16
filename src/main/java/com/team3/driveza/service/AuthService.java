package com.team3.driveza.service;

import com.team3.driveza.model.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Temporary register logic
    public User register(User user) {
        // Later: password encoding, validation, saving to DB
        return user;
    }

    // Temporary login logic
    public String login(User user) {
        // Later: validate password, return JWT
        return "Login successful (JWT will be added later)";
    }
}