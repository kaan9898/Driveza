package com.team3.driveza.Dto.Rental;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnRequestDto {
    @NotNull(message = "Latitude is required.")
    @Min(value = -90, message = "Latitude must be >= -90.")
    @Max(value = 90, message = "Latitude must be <= 90.")
    private Double latitude;

    @NotNull(message = "Longitude is required.")
    @Min(value = -180, message = "Longitude must be >= -180.")
    @Max(value = 180, message = "Longitude must be <= 180.")
    private Double longitude;

    private String photoUrl;
}
