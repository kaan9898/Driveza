package com.team3.driveza.model;

import com.team3.driveza.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id @GeneratedValue
    private long id;
    private String name;
    private String password;
    private String email;
    private ZonedDateTime dob;
    private Role role;
}
