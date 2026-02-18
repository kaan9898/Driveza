package com.team3.driveza.controller;


import com.team3.driveza.service.UserService;
import com.team3.driveza.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {


    private final UserService userService;
    private final VehicleService vehicleService;

    @GetMapping("/cars")
    public String cars(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            Model model) {
        var cars = vehicleService.getCars(q,lat,lon,radius,sort); // adding sorting part 
        model.addAttribute("cars", cars);
        return "cars"; }

    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        String email = authentication.getName();
        model.addAttribute("user", userService.getUserByEmail(email));
        return "account";
    }


    @GetMapping("/map")
    public String map(Model model){
         model.addAttribute("cars",vehicleService.getAvailableVehicles());
        return "map";
    }

}
