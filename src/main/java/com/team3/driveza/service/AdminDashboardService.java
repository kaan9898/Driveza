package com.team3.driveza.service;

import com.team3.driveza.model.enums.Role;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.repository.UserRepository;
import com.team3.driveza.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public long totalVehicles() {
        return vehicleRepository.count();
    }

    public long availableVehicles() {
        return vehicleRepository.countByStatus(VehicleStatus.AVAILABLE);
    }

    public long rentedVehicles() {
        return vehicleRepository.countByStatus(VehicleStatus.RENTED);
    }

    public long totalUsers() {
        return userRepository.count();
    }

    public long adminUsers() {
        return userRepository.countByRole(Role.ADMIN);
    }

    public long normalUsers() {
        return userRepository.countByRole(Role.USER);
    }
}