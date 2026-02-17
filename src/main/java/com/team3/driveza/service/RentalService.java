package com.team3.driveza.service;

import com.team3.driveza.model.Rental;
import com.team3.driveza.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    public Rental rentVehicle(Long vehicleId, Long userId) {
        return new Rental();
    }

    public Rental returnVehicle(Long rentalId, Double latitude, Double longitude) {
        return new Rental();
    }

    public Rental getRentalById(Long id) {
        return new Rental();
    }

    public List<Rental> getRentalsByUser(Long userId) {
        return new ArrayList<>();
    }
}
