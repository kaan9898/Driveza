package com.team3.driveza.service;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>();
    }

    public List<Vehicle> getAvailableVehicles() {
        return new ArrayList<>();
    }

    public Vehicle getVehicleById(Long id) {
        return new Vehicle();
    }

    public List<Vehicle> getNearbyVehicles(Double lat, Double lon, Double radius) {
        return new ArrayList<>();
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicle;
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        return vehicle;
    }

    public void deleteVehicle(Long id) {
    }
}