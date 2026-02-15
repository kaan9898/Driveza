package com.team3.driveza.Dto.Auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {
    @NotNull
    @NotBlank(message = "This field cannot be empty.")
    private String username;
    @NotNull
    @NotBlank(message = "This field cannot be empty.")
    private String password;
}
