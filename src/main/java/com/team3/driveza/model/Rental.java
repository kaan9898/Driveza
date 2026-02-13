package com.team3.driveza.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.time.ZonedDateTime;

public class Rental {
    @Id @GeneratedValue
    private long id;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private RentalStatus status;
    @OneToOne
    private User user;
    @OneToOne
    private Vehicle vehicle;
}
