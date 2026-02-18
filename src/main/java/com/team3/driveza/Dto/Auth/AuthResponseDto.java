package com.team3.driveza.Dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String message;
}
