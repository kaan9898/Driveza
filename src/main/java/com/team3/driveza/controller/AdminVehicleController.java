package com.team3.driveza.controller;

import com.team3.driveza.Dto.Vehicle.VehicleAdminResponseDto;
import com.team3.driveza.Dto.Vehicle.VehicleCreateDto;
import com.team3.driveza.service.VehicleModelService;
import com.team3.driveza.service.VehicleService;
import com.team3.driveza.service.VpicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public String listAdminVehicles(@RequestParam(required = false) Integer page, Model model) {
        if (page == null || page < 0) {
            page = 0;
        }
        var vehiclePage = vehicleService.getAllVehicles(PageRequest.of(page, 10));
        var currentPage=vehiclePage.getNumber();
        model.addAttribute("vehicles", vehiclePage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", Math.max(1, vehiclePage.getTotalPages()));
        model.addAttribute("prevHref", vehiclePage.hasPrevious() ? buildListHref(currentPage - 1) : null);
        model.addAttribute("nextHref", vehiclePage.hasNext() ? buildListHref(currentPage + 1) : null);
        return "admin/vehicles/list";
    }

    private String buildListHref(int page) {
        return UriComponentsBuilder.fromPath("/admin/vehicles")
                .queryParam("page", page)
                .build()
                .toUriString();
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
