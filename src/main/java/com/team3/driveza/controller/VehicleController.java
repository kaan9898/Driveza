package com.team3.driveza.controller;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.service.VehicleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UI endpoints for listing and editing vehicles via Thymeleaf.
 */
@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Show available vehicles on the index page.
    @GetMapping("/listVehicles")
    public String listVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.getAvailableVehicles(Pageable.unpaged(Sort.by("id"))));
        return "vehicles/list";
    }

    // Display detail view for a single vehicle.
    @GetMapping("/{id}")
    public String getVehicleById(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        return "vehicles/list";
    }

    // Render the form for registering a new vehicle.
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "vehicles/form";
    }

    // Handle vehicle creation submissions.
    @PostMapping
    public String createVehicle(@ModelAttribute Vehicle vehicle) {
        vehicleService.createVehicle(vehicle);
        return "redirect:/vehicles";
    }

    // Show edit form populated with the stored vehicle.
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        return "vehicles/form";
    }

    // Submit edits for a vehicle record.
    @PostMapping("/{id}/edit")
    public String updateVehicle(@PathVariable Long id, @ModelAttribute Vehicle vehicle) {
        vehicleService.updateVehicle(id, vehicle);
        return "redirect:/vehicles";
    }

    // Delete action triggered from the detail/form pages.
    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/vehicles";
    }

    // Optional nearby search that renders the same list page with filtered data.
    @GetMapping("/nearby")
    public String nearbyVehicles(@RequestParam Double latitude,
                                 @RequestParam Double longitude,
                                 @RequestParam Double radius,
                                 Model model) {
        model.addAttribute("vehicles", vehicleService.getCars(null, latitude, longitude, radius, null));
        return "vehicles/list";
    }
}
