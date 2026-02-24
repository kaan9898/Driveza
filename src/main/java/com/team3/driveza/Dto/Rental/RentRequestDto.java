package com.team3.driveza.Dto.Rental;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RentRequestDto {
    @NotNull(message = "Vehicle selection is required.")
    @Positive(message = "Vehicle ID must be a positive number.")
    private Long vehicleId;

    @NotNull(message = "User selection is required.")
    @Positive(message = "User ID must be a positive number.")
    private Long userId;
}
