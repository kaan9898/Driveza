package com.team3.driveza.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class User {
    @Id @GeneratedValue
    private long id;
    private String name;
    private String password;
    private String email;
    private Date dob;
    private Role role;
}
