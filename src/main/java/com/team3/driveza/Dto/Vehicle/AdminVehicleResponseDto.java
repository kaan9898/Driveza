package com.team3.driveza.Dto.Vehicle;

import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.model.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminVehicleResponseDto {
    private Long id;
    private String brand;
    private String modelName;
    private VehicleStatus status;
    private VehicleType type;
    private double pricePerMin;
    private double latitude;
    private double longitude;
}
