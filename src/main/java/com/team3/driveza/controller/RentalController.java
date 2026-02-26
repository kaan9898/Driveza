package com.team3.driveza.controller;

import com.team3.driveza.Dto.Rental.RentRequestDto;
import com.team3.driveza.Dto.Rental.RentalResponseDto;
import com.team3.driveza.Dto.Rental.ReturnRequestDto;
import com.team3.driveza.model.Rental;
import com.team3.driveza.service.RentalService;
import com.team3.driveza.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Rental endpoints: server-rendered pages plus JSON API paths when needed.
 * Accessible to authenticated users.
 */
@Controller
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;
    private final UserService userService;

    // Display the current user rentals on a Thymeleaf page.
    @GetMapping("/rentals")
    public String listRentals(@RequestParam(defaultValue = "1") Long userId, Model model) {
        List<RentalResponseDto> rentals = rentalService.mapToDtos(rentalService.getRentalsByUser(userId));
        model.addAttribute("rentals", rentals);
        return "rentals/list";
    }

    // Render a form that lets a user rent a vehicle.
    @GetMapping("/rentals/new")
    public String showRentForm(Model model) {
        model.addAttribute("rentRequest", new RentRequestDto());
        return "rentals/form";
    }

    // Process the rent form and redirect back to the rental list.
    @PostMapping("/rentals/rent")
    public String rentVehicle(@Valid @ModelAttribute("rentRequest") RentRequestDto rentRequest) {
        rentalService.rentVehicle(rentRequest.getVehicleId(), rentRequest.getUserId());
        return "redirect:/rentals?userId=" + rentRequest.getUserId();
    }

    // Render the detail page for a specific rental.
    @GetMapping("/rentals/{id}")
    public String rentalDetail(@PathVariable Long id, Model model) {
        model.addAttribute("rental", rentalService.mapToDto(rentalService.getRentalById(id)));
        return "rentals/detail";
    }

    // Handle return action triggered from the UI.
    @PostMapping("/rentals/{rentalId}/return")
    public String returnVehicleForm(@PathVariable Long rentalId,
                                    @Valid @ModelAttribute ReturnRequestDto returnRequest,
                                    Principal principal) {
        Rental rental = rentalService.getRentalById(rentalId);
        if (rental.getUser() == null || principal == null ||
                !principal.getName().equalsIgnoreCase(rental.getUser().getEmail())) {
            throw new AccessDeniedException("You can only return your own rental.");
        }

        rentalService.returnVehicle(rentalId, returnRequest.getLatitude(), returnRequest.getLongitude());

        return "redirect:/map?returnSuccess=true" + "&returnLat=" + returnRequest.getLatitude() + "&returnLon=" + returnRequest.getLongitude();
    }

    // API endpoint: rent a vehicle via path parameters.
    @PostMapping("/api/rentals/rent/{vehicleId}/user/{userId}")
    public ResponseEntity<Rental> rentVehicle(
            @PathVariable Long vehicleId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                rentalService.rentVehicle(vehicleId, userId)
        );
    }


    //    car rent
    @PostMapping("/rentals/{vehicleId}/rent")
    public String rentFromCard(@PathVariable Long vehicleId, Principal principal) {
        var user = userService.getUserByEmail(principal.getName());

        if (rentalService.getActiveRentalForUser(user.getId()).isPresent()) {
            return "redirect:/cars?alreadyRented";
        }
        rentalService.rentVehicle(vehicleId, user.getId());
        return "redirect:/cars"; //just for testing will need to implement
//        return "redirect:/cars:success";
    }

    // API endpoint: return a vehicle by ID.
    @PutMapping("/api/rentals/return/{rentalId}")
    public ResponseEntity<Rental> returnVehicle(
            @PathVariable Long rentalId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        return ResponseEntity.ok(
                rentalService.returnVehicle(rentalId, latitude, longitude)
        );
    }

    // API endpoint: retrieve rentals for a user.
    @GetMapping("/api/rentals/user/{userId}")
    public ResponseEntity<List<Rental>> getUserRentals(@PathVariable Long userId) {
        return ResponseEntity.ok(
                rentalService.getRentalsByUser(userId)
        );
    }

    @Setter
    @Getter
    private static class RentalForm {
        private Long vehicleId;
        private Long userId;

    }
}