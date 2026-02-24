package com.team3.driveza.Dto.Rental;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class RentalResponseDto {
    public enum Status {
        ACTIVE, COMPLETED
    }

    private Long id;
    private Long vehicleId;
    private String vehicleModel;
    private String vehicleType;
    private String userName;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Status status;
    private double pricePerMin;
}
