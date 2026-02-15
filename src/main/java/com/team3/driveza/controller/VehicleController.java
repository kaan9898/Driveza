package com.team3.driveza.controller;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.service.VehicleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User-facing endpoints for vehicles.
 * Allows listing available vehicles, fetching details, and nearby search.
 */
@RestController
@RequestMapping("/api/vehicles") // public user endpoints
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * List all available vehicles.
     * GET /api/vehicles
     *
     * @return List of available vehicles
     */
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAvailableVehicles() {
        return ResponseEntity.ok(vehicleService.getAvailableVehicles());
    }

    /**
     * Get details of a vehicle by ID.
     * GET /api/vehicles/{id}
     *
     * @param id Vehicle ID
     * @return Vehicle details
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    /**
     * Get vehicles nearby a given location.
     * GET /api/vehicles/nearby?latitude=..&longitude=..&radius=..
     *
     * @param latitude Latitude of user location
     * @param longitude Longitude of user location
     * @param radius Radius in km
     * @return List of nearby vehicles
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<Vehicle>> getNearbyVehicles(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radius) {
        return ResponseEntity.ok(vehicleService.getNearbyVehicles(latitude, longitude, radius));
    }
}