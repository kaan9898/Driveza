package com.team3.driveza.service;

import com.team3.driveza.model.Rental;
import com.team3.driveza.model.User;
import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.RentalStatus;
import com.team3.driveza.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final VehicleService vehicleService;
    private final UserService userService;

    // TODO: Use DTOs
    public Rental rentVehicle(long vehicleId, long userId) throws RuntimeException {
        User user = userService.findOrThrow(userId);
        Rental rental = new Rental();
        Vehicle vehicle = vehicleService.rentById(vehicleId);
        rental.setVehicle(vehicle);
        rental.setUser(user);
        rental.setStartTime(ZonedDateTime.now());
        rental.setStatus(RentalStatus.ACTIVE);
        return rentalRepository.save(rental);
    }

    public Rental returnVehicle(long rentalId, double latitude, double longitude) throws RuntimeException {
        Rental rental = findOrThrow(rentalId);
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new RuntimeException("This rental can't be returned.");
        }
        rental.setEndTime(ZonedDateTime.now());
        vehicleService.returnVehicle(rental.getVehicle(), latitude, longitude);
        return rentalRepository.save(rental);
    }

    public Rental getRentalById(long id) throws RuntimeException {
        return findOrThrow(id);
    }

    public List<Rental> getRentalsByUser(long userId) {
        return rentalRepository.findByUserId(userId);
    }

    public Rental findOrThrow(long id) throws RuntimeException {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This rental does not exist."));
    }
}
