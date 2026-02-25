package com.team3.driveza.controller;


import com.team3.driveza.Dto.Rental.RentalResponseDto;
import com.team3.driveza.Dto.Vehicle.VehicleUserResponseDto;
import com.team3.driveza.exception.ResourceNotFoundException;
import com.team3.driveza.model.Rental;
import com.team3.driveza.model.enums.RentalStatus;
import com.team3.driveza.service.RentalService;
import com.team3.driveza.service.UserService;
import com.team3.driveza.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PageController {


    private final UserService userService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        return isAdmin ? "redirect:/admin/dashboard" : "redirect:/cars";
    }

    @GetMapping("/cars")
    public String cars(
            @RequestParam Map<String, String> params,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }
        if (page == null || page < 0) {
            page = 0;
        }

        var cars = vehicleService.getVehicles(q, lat, lon, radius, sort, PageRequest.of(page, 6)); // adding sorting part
        model.addAttribute("cars", cars);

        model.addAttribute("currentPage", cars.getNumber());
        model.addAttribute("totalPages", Math.max(1, cars.getTotalPages()));
        model.addAttribute("prevHref", cars.hasPrevious() ? buildCarsHref(cars.getNumber() - 1, params) : null);
        model.addAttribute("nextHref", cars.hasNext() ? buildCarsHref(cars.getNumber() + 1, params) : null);

        var user = userService.getUserByEmail(principal.getName());
        model.addAttribute("userId", user.getId());
        // active rental
        model.addAttribute("activeRental",
                rentalService.getActiveRentalForUser(user.getId())
                        .map(rentalService::mapToDto)
                        .orElse(null));


        return "cars";
    }

    private String buildCarsHref(int page, Map<String, String> params) {
        UriComponentsBuilder b = UriComponentsBuilder.fromPath("/cars");
        params.put("page", String.valueOf(page));
        params.forEach(b::queryParam);
        // apply after iteration to replace

        return b.build().toUriString();
    }

    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        String email = authentication.getName();
        model.addAttribute("user", userService.getUserByEmail(email));
        return "account";
    }

    //loading map to ui and showing available vehicle location point in map
    @GetMapping("/map")
    public String map(@RequestParam(required = false) Double lat,
                      @RequestParam(required = false) Double lon,
                      @RequestParam(required = false) Double radius,
                      @RequestParam(required = false) Long returnRentalId,
                      @RequestParam(required = false) Double returnLat,
                      @RequestParam(required = false) Double returnLon,
                      @RequestParam(required = false) Boolean returnSuccess,
                      Model model,
                      Principal principal) {

        List<VehicleUserResponseDto> vehicleList = vehicleService.getVehicles(null, lat, lon, radius, null, PageRequest.ofSize(300)).toList();

        model.addAttribute("vehicles", vehicleList);
        model.addAttribute("lat", lat);
        model.addAttribute("lon", lon);
        model.addAttribute("radius", radius);

        RentalResponseDto returningRental = null;
        if (returnRentalId != null && principal != null) {
            try {
                Rental rental = rentalService.getRentalById(returnRentalId);
                if (rental.getUser() != null
                        && rental.getStatus() == RentalStatus.ACTIVE
                        && principal.getName().equalsIgnoreCase(rental.getUser().getEmail())) {
                    returningRental = rentalService.mapToDto(rental);
                }
            } catch (ResourceNotFoundException ignored) {
            }
        }

        model.addAttribute("returningRental", returningRental);
        model.addAttribute("returnSuccess", Boolean.TRUE.equals(returnSuccess));
        model.addAttribute("returnSuccessLat", returnLat);
        model.addAttribute("returnSuccessLon", returnLon);

        return "map";
    }

    @GetMapping("/account/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    @PostMapping("/account/change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, Authentication authentication) {
        String email = authentication.getName();
        try {
            userService.changePassword(email, oldPassword, newPassword, confirmPassword);
            return "redirect:/cars?passwordUpdated";
//            return "redirect:/account/change-password?success";
        } catch (RuntimeException e) {
            return "redirect:/account/change-password?error=" + e.getMessage();
        }
    }

    @GetMapping("/account/edit")
    public String editAccount(Model model, Authentication authentication) {
        String email = authentication.getName();
        model.addAttribute("user", userService.getUserByEmail(email));
        return "account-edit";
    }

    @PostMapping("/account/edit")
    public String updateAccount(@RequestParam String name, @RequestParam(required = false) String dob, Authentication authentication) {
        String email = authentication.getName();
        userService.updateProfile(email, name, dob);
        return "redirect:/account?updated";
    }


    @GetMapping("/support")
    public String supportPage(){
        return "support";
    }

    @GetMapping("/admin/support")
    public String adminSupportPage() {
        return "admin/support";
    }

}
