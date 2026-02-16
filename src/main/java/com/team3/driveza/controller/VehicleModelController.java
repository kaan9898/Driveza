package com.team3.driveza.controller;

import com.team3.driveza.model.VehicleModel;
import com.team3.driveza.service.VehicleModelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Admin-facing pages for managing vehicle models via Thymeleaf.
 */
@Controller
@RequestMapping("/models")
public class VehicleModelController {

    private final VehicleModelService vehicleModelService;

    public VehicleModelController(VehicleModelService vehicleModelService) {
        this.vehicleModelService = vehicleModelService;
    }

    // Shows the catalog of vehicle models.
    @GetMapping
    public String listModels(Model model) {
        model.addAttribute("models", vehicleModelService.getAllModels());
        return "models/list";
    }

    // Form for creating a new model entry.
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("model", new VehicleModel());
        return "models/form";
    }

    // Handle form submission for new models.
    @PostMapping
    public String createModel(@ModelAttribute VehicleModel vehicleModel) {
        vehicleModelService.createModel(vehicleModel);
        return "redirect:/models";
    }

    // Edit form for an existing model.
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("model", vehicleModelService.getModelById(id));
        return "models/form";
    }

    // Submits updates for a model.
    @PostMapping("/{id}/edit")
    public String updateModel(@PathVariable Long id, @ModelAttribute VehicleModel vehicleModel) {
        vehicleModelService.updateModel(id, vehicleModel);
        return "redirect:/models";
    }

    // Deletes a model entry.
    @PostMapping("/{id}/delete")
    public String deleteModel(@PathVariable Long id) {
        vehicleModelService.deleteModel(id);
        return "redirect:/models";
    }
}
