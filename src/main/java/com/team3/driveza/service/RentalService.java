package com.team3.driveza.service;

import com.team3.driveza.Dto.Rental.RentalResponseDto;
import com.team3.driveza.exception.ConflictException;
import com.team3.driveza.exception.ResourceNotFoundException;
import com.team3.driveza.model.Rental;
import com.team3.driveza.model.User;
import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.enums.RentalStatus;
import com.team3.driveza.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final VehicleService vehicleService;
    private final UserService userService;

    // TODO: Use DTOs
    public RentalResponseDto rentVehicle(long vehicleId, long userId) throws RuntimeException {
        User user = userService.findOrThrow(userId);
        Rental rental = new Rental();
        Vehicle vehicle = vehicleService.rentById(vehicleId);
        rental.setVehicle(vehicle);
        rental.setUser(user);
        rental.setStartTime(ZonedDateTime.now());
        rental.setStatus(RentalStatus.ACTIVE);
        return mapToDto(rentalRepository.save(rental));
    }

    public RentalResponseDto returnVehicle(long rentalId, double latitude, double longitude) throws ConflictException {
        Rental rental = findOrThrow(rentalId);
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new ConflictException("This rental can't be returned.");
        }
        rental.setEndTime(ZonedDateTime.now());
        rental.setStatus(RentalStatus.COMPLETED);
        vehicleService.returnVehicle(rental.getVehicle(), latitude, longitude);
        return mapToDto(rentalRepository.save(rental));
    }

    public RentalResponseDto getRentalById(long id) throws ResourceNotFoundException {
        return mapToDto(findOrThrow(id));
    }

    public List<RentalResponseDto> getRentalsByUser(long userId) {
        return mapToDtos(rentalRepository.findByUserId(userId));
    }

    public Rental findOrThrow(long id) throws ResourceNotFoundException {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found. id=" + id));
    }

    public RentalResponseDto getActiveRentalForUser(long userId) throws ResourceNotFoundException {
        var rental = rentalRepository.findFirstByUserIdAndStatus(userId, RentalStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found for user id=" + userId));
        return mapToDto(rental);
    }

    public List<RentalResponseDto> mapToDtos(List<Rental> rentals) {
        return rentals.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public RentalResponseDto mapToDto(Rental rental) {
        if (rental == null) {
            return null;
        }
        RentalResponseDto dto = new RentalResponseDto();
        dto.setId(rental.getId());
        dto.setStartDate(rental.getStartTime());
        dto.setEndDate(rental.getEndTime());
        dto.setStatus(rental.getStatus() != null ? rental.getStatus().name() : null);

        if (rental.getVehicle() != null) {
            Vehicle vehicle = rental.getVehicle();
            dto.setVehicleId(vehicle.getId());
            dto.setVehicleType(vehicle.getType() != null ? vehicle.getType().name() : null);
            dto.setPricePerMin(vehicle.getPricePerMin());
            if (vehicle.getModel() != null) {
                dto.setVehicleModel(vehicle.getModel().getBrand() + " " + vehicle.getModel().getModel());
            }
        }

        if (rental.getUser() != null) {
            dto.setUserName(rental.getUser().getEmail());
        }

        return dto;
    }
}
