package com.team3.driveza.controller;

import com.team3.driveza.model.Vehicle;
import com.team3.driveza.repository.VehicleModelRepository;
import com.team3.driveza.service.VehicleModelService;
import com.team3.driveza.service.VehicleService;
import com.team3.driveza.service.VpicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin UI for managing vehicles via server-rendered templates.
 */
@Controller
@RequestMapping("/admin/vehicles")
public class AdminVehicleController {

    private final VehicleService vehicleService;
    private final VehicleModelRepository vehicleModelRepository;
    private final VpicService vpicService;
    private final VehicleModelService vehicleModelService;

    public AdminVehicleController(VehicleService vehicleService, VehicleModelRepository vehicleModelRepository, VpicService vpicService, VehicleModelService vehicleModelService) {

        this.vehicleService = vehicleService;
        this.vehicleModelRepository = vehicleModelRepository;
        this.vpicService = vpicService;
        this.vehicleModelService = vehicleModelService;
    }

    // Admin list of all vehicles, including rented/unavailable.
    @GetMapping
    public String listAdminVehicles(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "admin/vehicles/list";
    }

    // Show admin form to add a new vehicle.
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("vehicleModels", vehicleModelRepository.findAll());
        model.addAttribute("makes", vpicService.getAllMakes());
        return "admin/vehicles/form";
    }

    // Create new vehicle from admin form.
    @PostMapping
    public String createVehicle(@ModelAttribute Vehicle vehicle, @RequestParam String brand, @RequestParam String modelName) {
        var vm = vehicleModelService.findOrCreate(brand, modelName);
        vehicle.setModel(vm);

        vehicleService.createVehicle(vehicle);
        return "redirect:/admin/vehicles";
    }

    // Detail view for a specific admin vehicle.
    @GetMapping("/{id}")
    public String vehicleDetail(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        return "admin/vehicles/detail";
    }

    // Show edit form pre-filled with vehicle data.
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", vehicleService.getVehicleById(id));
        model.addAttribute("models", vehicleModelRepository);
        return "admin/vehicles/form";
    }

    // Handle admin edits.
    @PostMapping("/{id}/edit")
    public String updateVehicle(@PathVariable Long id, @ModelAttribute Vehicle vehicle) {
        vehicleService.updateVehicle(id, vehicle);
        return "redirect:/admin/vehicles";
    }

    // Admin delete action.
    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/admin/vehicles";
    }
}
