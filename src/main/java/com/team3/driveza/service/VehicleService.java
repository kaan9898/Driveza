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

    public void createVehicle(Vehicle newVehicle) throws RuntimeException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(newVehicle.getModel());
        vehicle.setLongitude(newVehicle.getLongitude());
        vehicle.setLatitude(newVehicle.getLatitude());
        vehicle.setPricePerMin(newVehicle.getPricePerMin());
        vehicle.setType(newVehicle.getType());
        vehicle.setStatus(newVehicle.getStatus());
        vehicleRepository.save(vehicle);
    }

    public void updateVehicle(long id, Vehicle newVehicle) throws RuntimeException {
        Vehicle vehicle = findOrThrow(id);
        vehicle.setModel(newVehicle.getModel());
        vehicle.setLongitude(newVehicle.getLongitude());
        vehicle.setLatitude(newVehicle.getLatitude());
        vehicle.setPricePerMin(newVehicle.getPricePerMin());
        vehicle.setType(newVehicle.getType());
        vehicle.setStatus(newVehicle.getStatus());
        vehicleRepository.save(vehicle);
    }

    public Vehicle rentById(long id) throws RuntimeException {
        Vehicle vehicle = findOrThrow(id);
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new RuntimeException("Vehicle can't be rented.");
        }
        vehicle.setStatus(VehicleStatus.RENTED);
        return vehicleRepository.save(vehicle);
    }

    public void returnVehicle(Vehicle vehicle, double lat, double lon) throws RuntimeException {
        if (vehicle.getStatus() != VehicleStatus.RENTED) {
            throw new RuntimeException("Vehicle can't be returned.");
        }
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setLatitude(lat);
        vehicle.setLongitude(lon);
        vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(long id) throws RuntimeException {
        Vehicle vehicle = findOrThrow(id);
        vehicleRepository.delete(vehicle);
    }

    private Vehicle findOrThrow(long id) throws RuntimeException {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This vehicle does not exist."));
    }



    public List<Vehicle> getCars(String q, Double lat, Double lon, Double radiusKm, String sort) {

        // 1) base list (available only)
        List<Vehicle> cars;

        boolean hasLocation = lat != null && lon != null;
        boolean hasRadius = radiusKm != null;

        if (hasLocation && hasRadius) {
            cars = vehicleRepository.findAllWithinRadius(lat, lon, radiusKm);
            // keep only AVAILABLE if needed (radius query currently returns all)
            cars = cars.stream()
                    .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
                    .toList();
        } else if (q != null && !q.isBlank()) {
            cars = vehicleRepository.searchAvailable(VehicleStatus.AVAILABLE, q.trim());
        } else {
            cars = vehicleRepository.findAllByStatus(VehicleStatus.AVAILABLE);
        }

        // 2) sorting
        if ("priceAsc".equals(sort)) {
            cars = cars.stream()
                    .sorted((a, b) -> Double.compare(a.getPricePerMin(), b.getPricePerMin()))
                    .toList();
        }

        // distanceAsc needs lat/lon
        if ("distanceAsc".equals(sort) && hasLocation) {
            cars = cars.stream()
                    .sorted((a, b) -> Double.compare(
                            distanceKm(lat, lon, a.getLatitude(), a.getLongitude()),
                            distanceKm(lat, lon, b.getLatitude(), b.getLongitude())
                    ))
                    .toList();
        }

        return cars;
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double aa = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        return 2 * R * Math.atan2(Math.sqrt(aa), Math.sqrt(1-aa));
    }


}