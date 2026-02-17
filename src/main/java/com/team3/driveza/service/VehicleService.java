package com.team3.driveza.service;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    // TODO: Use DTOs
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findAllByStatus(VehicleStatus.AVAILABLE);
    }

    public Vehicle getVehicleById(long id) throws RuntimeException {
        return findOrThrow(id);
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
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(long id, Vehicle newVehicle) throws RuntimeException {
        Vehicle vehicle = findOrThrow(id);
        vehicle.setModel(newVehicle.getModel());
        vehicle.setLongitude(newVehicle.getLongitude());
        vehicle.setLatitude(newVehicle.getLatitude());
        vehicle.setPricePerMin(newVehicle.getPricePerMin());
        vehicle.setType(newVehicle.getType());
        vehicle.setStatus(newVehicle.getStatus());
        return vehicleRepository.save(vehicle);
    }

    public Vehicle rentById(long id) throws RuntimeException {
        Vehicle vehicle = findOrThrow(id);
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new RuntimeException("Vehicle can't be rented.");
        }
        vehicle.setStatus(VehicleStatus.RENTED);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle returnVehicle(Vehicle vehicle, double lat, double lon) throws RuntimeException {
        if (vehicle.getStatus() != VehicleStatus.RENTED) {
            throw new RuntimeException("Vehicle can't be returned.");
        }
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setLatitude(lat);
        vehicle.setLongitude(lon);
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(long id) throws RuntimeException {
        Vehicle vehicle = findOrThrow(id);
        vehicleRepository.delete(vehicle);
    }

    private Vehicle findOrThrow(long id) throws RuntimeException {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This vehicle does not exist."));
    }

}