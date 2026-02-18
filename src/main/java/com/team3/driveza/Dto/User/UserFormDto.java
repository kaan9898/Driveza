package com.team3.driveza.Dto.User;

import com.team3.driveza.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserFormDto {
    private Long id;

    @NotBlank(message = "Name is required.")
    private String name;

    @Email(message = "Must be a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    private Role role;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
}
