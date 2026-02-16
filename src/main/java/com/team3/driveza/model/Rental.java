package com.team3.driveza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class Rental {
    @Id @GeneratedValue
    private long id;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private RentalStatus status;
    @OneToOne(cascade = CascadeType.DETACH)
    private User user;
    @OneToOne(cascade = CascadeType.DETACH)
    private Vehicle vehicle;
}
