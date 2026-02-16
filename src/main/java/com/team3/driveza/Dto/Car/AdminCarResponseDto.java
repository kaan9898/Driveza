package com.team3.driveza.Dto.Car;

public class AdminCarResponseDto {
    private int carId;
    private String brand;
    private String model;
    private enum statusEnum {
        ACTIVE, INACTIVE
    }
    private statusEnum status;
    private double pricePerKm;
    private int fuelLevel;
    private int latitude;
    private int longitude;
}
