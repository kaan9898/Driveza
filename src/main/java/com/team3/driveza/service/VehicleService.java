package com.team3.driveza.service;

import com.team3.driveza.exception.ConflictException;
import com.team3.driveza.exception.ResourceNotFoundException;
import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    // TODO: Use DTOs
    public long getVehicleCount() {
        return vehicleRepository.count();
    }

    public long getVehicleCountByStatus(VehicleStatus vehicleStatus) {
        return vehicleRepository.countByStatus(vehicleStatus);
    }

    public Page<Vehicle> getAllVehicles(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    public Page<Vehicle> getAvailableVehicles(Pageable pageable) {
        return vehicleRepository.findAllByStatus(VehicleStatus.AVAILABLE, pageable);
    }

    public Vehicle getVehicleById(long id) throws ResourceNotFoundException {
        return findOrThrow(id);
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

    public Vehicle rentById(long id) throws ConflictException {
        Vehicle vehicle = findOrThrow(id);
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new ConflictException("Vehicle can't be rented.");
        }
        vehicle.setStatus(VehicleStatus.RENTED);
        return vehicleRepository.save(vehicle);
    }

    public void returnVehicle(Vehicle vehicle, double lat, double lon) throws ConflictException {
        if (vehicle.getStatus() != VehicleStatus.RENTED) {
            throw new ConflictException("Vehicle can't be returned.");
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

    private Vehicle findOrThrow(long id) throws ResourceNotFoundException {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found. id=" + id));
    }

    public Page<Vehicle> getCars(String q, Double lat, Double lon, Double radiusKm, String sortString) {
        boolean hasText = q != null && !q.isBlank();
        boolean hasLocation = lat != null && lon != null && radiusKm != null;

        sortString = sortString != null ? sortString : "";

        Sort sort;
        if (sortString.equals("priceAsc")) {
            sort = Sort.by(Sort.Direction.ASC, "pricePerMin");
        } else if (sortString.equals("distanceAsc") && hasLocation) {
            sort = Sort.by(Sort.Direction.ASC, "distance");
        } else {
            sort = Sort.by("id");
        }

        Pageable pageable = Pageable.unpaged(sort);

        if (hasLocation && hasText) {
            return vehicleRepository.findAllWithinRadiusAndName(lat, lon, radiusKm, VehicleStatus.AVAILABLE.name(), q.trim(), pageable);
        } else if (hasText) {
            return vehicleRepository.searchAvailable(q.trim(), pageable);
        } else if (hasLocation) {
            return vehicleRepository.findAllWithinRadius(lat, lon, radiusKm, VehicleStatus.AVAILABLE.name(), pageable);
        }
        return vehicleRepository.findAllByStatus(VehicleStatus.AVAILABLE, pageable);
    }
}
