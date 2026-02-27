package com.team3.driveza.controller;

import com.team3.driveza.Dto.Vehicle.VehicleUserResponseDto;
import com.team3.driveza.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * UI endpoints for listing and editing vehicles via Thymeleaf.
 */
@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleUserController {
    private final VehicleService vehicleService;

    // Show available vehicles on the index page.
    @GetMapping("/nearby")
    public Page<VehicleUserResponseDto> nearby(
            @RequestParam Double radius,
            @RequestParam Double lat,
            @RequestParam Double lon
    ) {
        return vehicleService.getVehicles(null, lat, lon, radius, null, PageRequest.ofSize(300));
    }

    @GetMapping("/filter")
    public Page<VehicleUserResponseDto> filter(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon
    ) {
        if (page == null || page < 0) {
            page = 0;
        }

        return vehicleService.getVehicles(q, lat, lon, radius, sort, PageRequest.of(page, 6)); // adding sorting part
    }
}
