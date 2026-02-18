package com.team3.driveza.Dto.User;

import com.team3.driveza.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDate dob;
}
