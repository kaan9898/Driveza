package com.team3.driveza.Dto.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Name is required.")
    private String name;

    @Email(message = "Must be a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be in the past.")
    private LocalDate dob;
}
