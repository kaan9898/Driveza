package com.team3.driveza.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Vehicle {
    @Id @GeneratedValue
    private long id;
    @ManyToOne @JoinColumn
    private VehicleModel model;
    private double longitude;
    private double latitude;
    private double pricePerMin;
    private VehicleType type;
    private VehicleStatus status;
}
