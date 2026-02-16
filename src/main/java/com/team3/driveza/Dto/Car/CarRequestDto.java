package com.team3.driveza.Dto.Car;

public class CarRequestDto {
    private String model;
    private String brand;
    private double pricePerKm;
    private int fuelLevel;
    private int latitude;
    private int longitude;
    private enum  carStatus {
        ACTIVE, INACTIVE
    }
    private carStatus carStatus;
}
