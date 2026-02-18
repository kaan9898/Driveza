package com.team3.driveza.model;

import com.team3.driveza.model.User;
import com.team3.driveza.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @Past
    private ZonedDateTime dob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
