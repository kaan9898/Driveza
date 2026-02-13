package com.team3.driveza.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vehicle {
    @Id @GeneratedValue
    private long id;
    @ManyToOne @JoinColumn(name = "model_id")
    private VehicleModel model;
    private double longitude;
    private double latitude;
    private double pricePerMin;
    private VehicleType type;
    private VehicleStatus status;
}
