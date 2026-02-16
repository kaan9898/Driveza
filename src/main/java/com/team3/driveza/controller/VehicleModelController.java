package com.team3.driveza.controller;

import com.team3.driveza.model.VehicleModel;
import com.team3.driveza.service.VehicleModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints to manage vehicle models (brand + model).
 * Admin only for CRUD operations.
 */
@RestController
@RequestMapping("/api/models")
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    public VehicleModelController(VehicleModelService vehicleModelService) {
        this.vehicleModelService = vehicleModelService;
    }

    /**
     * List all vehicle models
     * GET /api/models
     *
     * @return List of vehicle models
     */
    @GetMapping
    public ResponseEntity<List<VehicleModel>> getAllModels() {
        return ResponseEntity.ok(vehicleModelService.getAllModels());
    }

    /**
     * Create a new vehicle model
     * POST /api/models
     *
     * @param model VehicleModel object
     * @return Created vehicle model
     */
    @PostMapping
    public ResponseEntity<VehicleModel> createModel(
            @RequestBody VehicleModel vehicleModel) {

        return ResponseEntity.ok(
                vehicleModelService.createModel(vehicleModel)
        );
    }

    /**
     * Delete a vehicle model
     * DELETE /api/models/{id}
     *
     * @param id VehicleModel ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteModel(@PathVariable Long id) {
        vehicleModelService.deleteModel(id);
        return ResponseEntity.ok("Model deleted successfully");
    }
}