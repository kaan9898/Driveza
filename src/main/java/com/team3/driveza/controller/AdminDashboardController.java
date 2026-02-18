package com.team3.driveza.controller;


import com.team3.driveza.service.AdminDashboardService;
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

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalVehicles", adminDashboardService.totalVehicles());
        model.addAttribute("availableVehicles", adminDashboardService.availableVehicles());
        model.addAttribute("rentedVehicle", adminDashboardService.rentedVehicles());
        model.addAttribute("totalUsers", adminDashboardService.totalUsers());
        model.addAttribute("adminUsers", adminDashboardService.adminUsers());
        model.addAttribute("normalUsers", adminDashboardService.normalUsers());
        return "admin/dashboard"; // templates/admin/dashboard.html
    }
}
