package com.team3.driveza.model;

import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.model.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn
    private VehicleModel model;
    @Range(min = -180, max = 180)
    private double longitude;
    @Range(min = -90, max = 90)
    private double latitude;
    @PositiveOrZero
    private double pricePerMin;
    private VehicleType type;
    private VehicleStatus status;
}
