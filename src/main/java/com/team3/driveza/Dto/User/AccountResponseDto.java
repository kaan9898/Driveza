package com.team3.driveza.Dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    private Long id;
    private String name;
    private String email;
    private String role;
}
