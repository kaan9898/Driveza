package com.team3.driveza.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class VehicleModel {
    @Id @GeneratedValue
    private long id;
    private String brand;
    private String model;
}
