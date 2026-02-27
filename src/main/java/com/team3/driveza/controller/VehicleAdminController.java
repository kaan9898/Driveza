package com.team3.driveza.controller;

import com.team3.driveza.Dto.Vehicle.VehicleAdminResponseDto;
import com.team3.driveza.Dto.Vehicle.VehicleCreateDto;
import com.team3.driveza.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


/**
 * UI endpoints for listing and editing vehicles via Thymeleaf.
 */
@RestController
@RequestMapping("/api/admin/vehicles")
@RequiredArgsConstructor
public class VehicleAdminController {
    private final VehicleService vehicleService;

    // Show available vehicles on the index page.
    @GetMapping
    public Page<VehicleAdminResponseDto> list(@RequestParam(required = false) Integer page) {
        if (page == null || page < 0) {
            page = 0;
        }
        return vehicleService.getAllVehicles(PageRequest.of(page, 10));
    }

    // Display detail view for a single vehicle.
    @GetMapping("/{id}")
    public VehicleAdminResponseDto getVehicleById(@PathVariable Long id) {
        return vehicleService.getAdminVehicleById(id);
    }

    @PostMapping
    public VehicleAdminResponseDto createVehicle(@RequestBody VehicleCreateDto dto) {
        return vehicleService.createVehicle(dto);
    }

    // Show edit form populated with the stored vehicle.
    @PutMapping("/{id}/edit")
    public VehicleAdminResponseDto update(@PathVariable Long id, @RequestBody VehicleCreateDto dto) {
        return vehicleService.updateVehicle(id, dto);
    }

    // Delete action triggered from the detail/form pages.
    @DeleteMapping("/{id}/delete")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }

    // Optional nearby search that renders the same list page with filtered data.
}
