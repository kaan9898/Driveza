package com.team3.driveza.Dto.Rental;

import java.util.Date;

public class RentalResponseDto {
    private int id;
    private int carId;
    private String model;
    private Date startDate;
    private Date endDate;
    private enum statusEnum {
        ACTIVE, INACTIVE
    }
    private statusEnum status;
    private double pricePerMin;
}
