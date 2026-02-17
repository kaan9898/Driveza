package com.team3.driveza.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle_model")
@Data
@NoArgsConstructor
public class VehicleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
}
