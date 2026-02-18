package com.team3.driveza.Dto.User;

import com.team3.driveza.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDate dob;
    private ZonedDateTime createdAt;
}
