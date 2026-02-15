package com.team3.driveza.Dto.Auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {

    @NotBlank(message = "This field cannot be empty.")
    private String username;
    @NotBlank(message = "This field cannot be empty.")
    private String password;
}
