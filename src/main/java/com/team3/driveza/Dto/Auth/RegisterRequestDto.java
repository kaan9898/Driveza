package com.team3.driveza.Dto.Auth;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequestDto {
    @NotBlank(message = "This field cannot be empty.")
    private String username;
    @NotBlank(message = "This field cannot be empty.")
    private String password;
    @NotBlank(message = "This field cannot be empty.")
    private String email;
}
