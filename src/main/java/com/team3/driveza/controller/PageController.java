package com.team3.driveza.controller;


import com.team3.driveza.model.User;
import com.team3.driveza.model.Vehicle;
import com.team3.driveza.service.RentalService;
import com.team3.driveza.service.UserService;
import com.team3.driveza.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {


    private final UserService userService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;

    @GetMapping("/cars")
    public String cars(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            Principal principal, Model model) {
        var cars = vehicleService.getCars(q,lat,lon,radius,sort); // adding sorting part 
        model.addAttribute("cars", cars);

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
//        System.out.println(principal.getName());
        // active rental
        model.addAttribute("activeRental", rentalService.getActiveRentalForUser(user.getId()).orElse(null));


        return "cars"; }

    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        String email = authentication.getName();
        model.addAttribute("user", userService.getUserByEmail(email));
        return "account";
    }

//loading map to ui and showing available vehicle location point in map
    @GetMapping("/map")
    public String map(@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon,
    @RequestParam(required = false) Double radius,
            Model model ){

        List<Vehicle> vehicleList;
        if (lat == null || lon == null) {

        vehicleList =vehicleService.getAvailableVehicles();
        }else{
            vehicleList = vehicleService.getNearbyVehicles(lat,lon,radius);

            if(vehicleList.isEmpty()){
                vehicleList = vehicleService.getAvailableVehicles();
            }
        }
        System.out.println("MAP vehicle count : "+ vehicleList.size());
        model.addAttribute("vehicles", vehicleList);
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        model.addAttribute("radius", radius);
//        model.addAttribute("cars", vehicleService.getAvailableVehicles());
        return "map";
//        }
//         System.out.println("available vehicle" + vehicleService.getAvailableVehicles().size());
//        System.out.println("nearby vehicle" + vehicleService.getNearbyVehicles(lat, lon, radious).size());
//        return "map";
    }

}
