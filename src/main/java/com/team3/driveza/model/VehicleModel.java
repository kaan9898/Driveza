package com.team3.driveza.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle_model")
@Data
@NoArgsConstructor
public class VehicleModel {
    @Id @GeneratedValue
    private long id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
}
