package com.team3.driveza.service;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    // TODO: Use DTOs
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findAllByStatus(VehicleStatus.AVAILABLE);
    }

    public Vehicle getVehicleById(long id) throws RuntimeException {
        return getVehicleWithoutDTO(id);
    }

    public List<Vehicle> getNearbyVehicles(Double lat, Double lon, Double radiusInKM) {
        return vehicleRepository.findAllByFormula(lat, lon, radiusInKM);
    }

    public Vehicle createVehicle(Vehicle newVehicle) throws RuntimeException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(newVehicle.getModel());
        vehicle.setLongitude(newVehicle.getLongitude());
        vehicle.setLatitude(newVehicle.getLatitude());
        vehicle.setPricePerMin(newVehicle.getPricePerMin());
        vehicle.setType(newVehicle.getType());
        vehicle.setStatus(newVehicle.getStatus());
        return vehicle;
    }

    public Vehicle updateVehicle(long id, Vehicle newVehicle) throws RuntimeException {
        Vehicle vehicle = getVehicleWithoutDTO(id);
        vehicle.setModel(newVehicle.getModel());
        vehicle.setLongitude(newVehicle.getLongitude());
        vehicle.setLatitude(newVehicle.getLatitude());
        vehicle.setPricePerMin(newVehicle.getPricePerMin());
        vehicle.setType(newVehicle.getType());
        vehicle.setStatus(newVehicle.getStatus());
        return vehicle;
    }

    public void deleteVehicle(long id) throws RuntimeException {
        Vehicle vehicle = getVehicleWithoutDTO(id);
        vehicleRepository.delete(vehicle);
    }

    public Vehicle getVehicleWithoutDTO(long id) throws RuntimeException {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            throw new RuntimeException("This vehicle does not exist.");
        }
        return optionalVehicle.get();
    }
}