package com.team3.driveza.controller;


import com.team3.driveza.model.enums.Role;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.service.UserService;
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
    private final UserService userService;
    private final VehicleService vehicleService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long totalVehicle = vehicleService.getVehicleCount();
        long availableVehicles = vehicleService.getVehicleCountByStatus(VehicleStatus.AVAILABLE);
        long rentedVehicles = vehicleService.getVehicleCountByStatus(VehicleStatus.RENTED);
        model.addAttribute("totalVehicles", totalVehicle);
        model.addAttribute("availableVehicles", availableVehicles);
        model.addAttribute("rentedVehicles", rentedVehicles);
        model.addAttribute("totalUsers", userService.getUserCount());
        model.addAttribute("adminUsers", userService.getUserCountByRole(Role.ADMIN));
        model.addAttribute("normalUsers", userService.getUserCountByRole(Role.USER));

        System.out.println("TOTAL: " + totalVehicle);
        System.out.println("AVAILABLE: " + availableVehicles);
        System.out.println("RENTED: " + rentedVehicles);
        return "admin/dashboard"; // templates/admin/dashboard.html
    }
}
