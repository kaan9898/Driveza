package com.team3.driveza.Dto.Rental;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class RentalResponseDto {
    private Long id;
    private Long vehicleId;
    private String vehicleModel;
    private String vehicleType;
    private String userName;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String status;
    private double pricePerMin;
}
