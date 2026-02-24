package com.team3.driveza.controller;

import com.team3.driveza.Dto.Vehicle.VehicleAdminResponseDto;
import com.team3.driveza.Dto.Vehicle.VehicleCreateDto;
import com.team3.driveza.service.VehicleModelService;
import com.team3.driveza.service.VehicleService;
import com.team3.driveza.service.VpicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Admin UI for managing vehicles via server-rendered templates.
 */
@Controller
@RequestMapping("/admin/vehicles")
@RequiredArgsConstructor
public class AdminVehicleController {
    private final VehicleService vehicleService;
    private final VpicService vpicService;
    private final VehicleModelService vehicleModelService;

    // Admin list of all vehicles, including rented/unavailable.
    @GetMapping
    public String listAdminVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles(Pageable.unpaged()));
        return "admin/vehicles/list";
    }

    // Show admin form to add a new vehicle.
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("vehicleId", 0);
        model.addAttribute("vehicle", new VehicleCreateDto());
        model.addAttribute("vehicleModels", vehicleModelService.getAllModels());
        model.addAttribute("makes", vpicService.getAllMakes());
        return "admin/vehicles/form";
    }

    // Create new vehicle from admin form.
    @PostMapping
    public String createVehicle(@ModelAttribute VehicleCreateDto vehicle, @RequestParam String brand, @RequestParam String modelName) {
        var vm = vehicleModelService.findOrCreate(brand, modelName);
        vehicle.setModelId(vm.getId());

        vehicleService.createVehicle(vehicle);
        return "redirect:/admin/vehicles";
    }

    // Detail view for a specific admin vehicle.
    @GetMapping("/{id}")
    public String vehicleDetail(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.getAdminVehicleById(id));
        return "admin/vehicles/detail";
    }

    // Show edit form pre-filled with vehicle data.
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("vehicleId", id);
        model.addAttribute("models", vehicleModelService.getAllModels());
        return "admin/vehicles/form";
    }

    // Handle admin edits.
    @PostMapping("/{id}/edit")
    public String updateVehicle(@PathVariable Long id, @ModelAttribute VehicleAdminResponseDto vehicle) {
        vehicleService.updateVehicle(id, VehicleCreateDto.builder()
                .modelId(vehicle.getModelId())
                .status(vehicle.getStatus())
                .type(vehicle.getType())
                .pricePerMin(vehicle.getPricePerMin())
                .latitude(vehicle.getLatitude())
                .longitude(vehicle.getLongitude())
                .build());
        return "redirect:/admin/vehicles";
    }

    // Admin delete action.
    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/admin/vehicles";
    }

    @PostMapping("/delete")
    public String deleteVehicleByParam(@RequestParam Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return "redirect:/admin/vehicles";
    }
}
