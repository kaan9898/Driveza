package com.team3.driveza.Dto.Vehicle;

import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.model.enums.VehicleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequestDto {
    private Long id;

    @NotNull(message = "Model selection is required.")
    private Long modelId;

    @NotNull(message = "Status is required.")
    private VehicleStatus status;

    @NotNull(message = "Type is required.")
    private VehicleType type;

    @Min(value = 0, message = "Price per minute must be non-negative.")
    private double pricePerMin;

    @Min(value = -90, message = "Latitude must be >= -90.")
    @Max(value = 90, message = "Latitude must be <= 90.")
    private double latitude;

    @Min(value = -180, message = "Longitude must be >= -180.")
    @Max(value = 180, message = "Longitude must be <= 180.")
    private double longitude;
}
