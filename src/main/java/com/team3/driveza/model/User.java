package com.team3.driveza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class User {
    @Id @GeneratedValue
    private long id;
    private String name;
    private String password;
    private String email;
    private ZonedDateTime dob;
    private Role role;
}
