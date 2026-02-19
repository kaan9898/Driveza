package com.team3.driveza.controller;


import com.team3.driveza.service.AdminDashboardService;
import com.team3.driveza.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {


    private final AdminDashboardService adminDashboardService;
    private final VehicleService vehicleService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long totalVehicle = vehicleService.getAllVehicles().size();
        long availableVehicles = vehicleService.getAvailableVehicles().size();
        long rentedVehicles = totalVehicle - availableVehicles;
        model.addAttribute("totalVehicles", totalVehicle);
        model.addAttribute("availableVehicles", availableVehicles);
        model.addAttribute("rentedVehicles", rentedVehicles);
        model.addAttribute("totalUsers", adminDashboardService.totalUsers());
        model.addAttribute("adminUsers", adminDashboardService.adminUsers());
        model.addAttribute("normalUsers", adminDashboardService.normalUsers());

        System.out.println("TOTAL: " + totalVehicle);
        System.out.println("AVAILABLE: " + availableVehicles);
        System.out.println("RENTED: " + rentedVehicles);
        return "admin/dashboard"; // templates/admin/dashboard.html
    }
}
