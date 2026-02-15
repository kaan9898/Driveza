package com.team3.driveza.controller;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.service.VehicleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin endpoints for managing vehicles.
 * Only accessible by admins.
 */
@RestController
@RequestMapping("/api/admin/vehicles") // admin-only endpoints
public class AdminVehicleController {

    private final VehicleService vehicleService;

    public AdminVehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * List all vehicles (admin)
     * GET /api/admin/vehicles
     *
     * @return List of all vehicles including RENTED/AVAILABLE/UNAVAILABLE
     */
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(
                vehicleService.getAllVehicles()
        );
    }

    /**
     * Create a new vehicle.
     * POST /api/admin/vehicles
     *
     * @param vehicle Vehicle object
     * @return Created vehicle
     */
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle saved = vehicleService.createVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Update an existing vehicle.
     * PUT /api/admin/vehicles/{id}
     *
     * @param id Vehicle ID
     * @param vehicle Updated vehicle info
     * @return Updated vehicle
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id,
            @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(
                vehicleService.updateVehicle(id, vehicle)
        );
    }

    /**
     * Delete a vehicle by ID.
     * DELETE /api/admin/vehicles/{id}
     *
     * @param id Vehicle ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}