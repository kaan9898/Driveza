package com.team3.driveza.controller;

import com.team3.driveza.model.Rental;
import com.team3.driveza.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rental endpoints: rent, return, and view rentals.
 * Accessible to authenticated users.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Rent a vehicle
     * POST /api/rentals/rent/{vehicleId}/user/{userId}
     *
     * @param vehicleId Vehicle to rent
     * @param userId User renting the vehicle
     * @return Created rental object
     */
    @PostMapping("/rent/{vehicleId}/user/{userId}")
    public ResponseEntity<Rental> rentVehicle(
            @PathVariable Long vehicleId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                rentalService.rentVehicle(vehicleId, userId)
        );
    }

    /**
     /**
     * Return a rented vehicle
     * PUT /api/rentals/return/{rentalId}?latitude=..&longitude=..
     *
     * @param rentalId Rental ID
     * @param latitude Drop-off latitude
     * @param longitude Drop-off longitude
     * @return Updated rental object
     */
    @PutMapping("/return/{rentalId}")
    public ResponseEntity<Rental> returnVehicle(
            @PathVariable Long rentalId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        return ResponseEntity.ok(
                rentalService.returnVehicle(rentalId, latitude, longitude)
        );
    }

    /**
     * Get all rentals by user
     * GET /api/rentals/user/{userId}
     *
     * @param userId User ID
     * @return List of rentals for this user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rental>> getUserRentals(@PathVariable Long userId) {
        return ResponseEntity.ok(
                rentalService.getRentalsByUser(userId)
        );
    }
}