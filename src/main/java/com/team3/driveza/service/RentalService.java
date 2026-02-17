package com.team3.driveza.service;

import com.team3.driveza.model.Rental;
import com.team3.driveza.model.User;
import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.RentalStatus;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;

    // TODO: Use DTOs
    public Rental rentVehicle(long vehicleId, long userId) throws RuntimeException {
        Vehicle vehicle = vehicleService.getVehicleWithoutDTO(vehicleId);
        switch (vehicle.getStatus()) {
            case RENTED -> throw new RuntimeException("This vehicle has been rented already.");
            case OUT_OF_SERVICE -> throw new RuntimeException("This vehicle is out of service.");
            case AVAILABLE -> {}
            default -> throw new RuntimeException("This vehicle is unavailable.");
        }
        User user = userService.getUserWithoutDTO(userId);
        Rental rental = new Rental();
        rental.setVehicle(vehicle);
        rental.setUser(user);
        rental.setStartTime(ZonedDateTime.now());
        rental.setStatus(RentalStatus.ACTIVE);

        vehicle.setStatus(VehicleStatus.RENTED);
        return new Rental();
    }

    public Rental returnVehicle(long rentalId, double latitude, double longitude) throws RuntimeException {
        Rental rental = getRentalWithoutDTO(rentalId);
        switch (rental.getStatus()) {
            case ACTIVE -> {}
            case COMPLETED -> throw new RuntimeException("This rental has been completed.");
            default -> throw new RuntimeException("This rental has invalid state.");
        }
        rental.setEndTime(ZonedDateTime.now());
        Vehicle vehicle = rental.getVehicle();
        switch (vehicle.getStatus()) {
            case RENTED -> {}
            case OUT_OF_SERVICE -> throw new RuntimeException("This vehicle is out of service.");
            case AVAILABLE -> throw new RuntimeException("Can't return this vehicle.");
            default -> throw new RuntimeException("This vehicle is unavailable.");
        }
        vehicle.setLatitude(latitude);
        vehicle.setLongitude(longitude);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        return rental;
    }

    public Rental getRentalById(long id) throws RuntimeException {
        return getRentalWithoutDTO(id);
    }

    public List<Rental> getRentalsByUser(long userId) {
        return rentalRepository.findByUserId(userId);
    }

    public Rental getRentalWithoutDTO(long id) throws RuntimeException {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isEmpty()) {
            throw new RuntimeException("This rental does not exist.");
        }
        return optionalRental.get();
    }
}
