package com.team3.driveza.service;

import com.team3.driveza.Dto.Vehicle.VehicleAdminResponseDto;
import com.team3.driveza.exception.ConflictException;
import com.team3.driveza.exception.ResourceNotFoundException;
import com.team3.driveza.Dto.Vehicle.VehicleCreateDto;
import com.team3.driveza.Dto.Vehicle.VehicleUserResponseDto;
import com.team3.driveza.model.Vehicle;
import com.team3.driveza.model.VehicleModel;
import com.team3.driveza.model.enums.VehicleStatus;
import com.team3.driveza.repository.VehicleRepository;
import com.team3.driveza.repository.VehicleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleModelService vehicleModelService;

    public long getVehicleCount() {
        return vehicleRepository.count();
    }

    public long getVehicleCountByStatus(VehicleStatus vehicleStatus) {
        return vehicleRepository.countByStatus(vehicleStatus);
    }

    public Page<VehicleAdminResponseDto> getAllVehicles(Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(this::toAdminResponseDto);
    }

    public VehicleAdminResponseDto getAdminVehicleById(long id) throws ResourceNotFoundException {
        return toAdminResponseDto(findOrThrow(id));
    }

    public void createVehicle(VehicleCreateDto newVehicle) throws RuntimeException {
        vehicleRepository.save(Vehicle.builder()
                .model(vehicleModelService.findOrThrow(newVehicle.getModelId()))
                .longitude(newVehicle.getLongitude())
                .latitude(newVehicle.getLatitude())
                .pricePerMin(newVehicle.getPricePerMin())
                .type(newVehicle.getType())
                .status(newVehicle.getStatus())
                .build());
    }

    public void updateVehicle(long id, VehicleCreateDto newVehicle) throws ResourceNotFoundException {
        findOrThrow(id);
        vehicleRepository.save(Vehicle.builder()
                .id(id)
                .model(vehicleModelService.findOrThrow(newVehicle.getModelId()))
                .longitude(newVehicle.getLongitude())
                .latitude(newVehicle.getLatitude())
                .pricePerMin(newVehicle.getPricePerMin())
                .type(newVehicle.getType())
                .status(newVehicle.getStatus())
                .build());
    }

    public Vehicle rentById(long id) throws ConflictException {
        Vehicle vehicle = findOrThrow(id);
        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new ConflictException("Vehicle can't be rented.");
        }
        vehicle.setStatus(VehicleStatus.RENTED);
        return vehicleRepository.save(vehicle);
    }

    public void returnVehicle(Vehicle vehicle, double lat, double lon) throws ConflictException {
        if (vehicle.getStatus() != VehicleStatus.RENTED) {
            throw new ConflictException("Vehicle can't be returned.");
        }
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setLatitude(lat);
        vehicle.setLongitude(lon);
        vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) throws ResourceNotFoundException {
        Vehicle vehicle = findOrThrow(id);
        vehicleRepository.delete(vehicle);
    }

    private Vehicle findOrThrow(Long id) throws ResourceNotFoundException {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found. id=" + id));
    }

    public Page<VehicleUserResponseDto> getVehicles(String q, Double lat, Double lon, Double radiusKm, String sortString, PageRequest pageRequest) {
        boolean hasText = q != null && !q.isBlank();
        if (hasText) {
            q = q.trim();
        }
        boolean hasLocation = lat != null && lon != null && radiusKm != null;

        sortString = sortString != null ? sortString : "";

        Sort sort;
        if (sortString.equals("priceAsc")) {
            sort = Sort.by(Sort.Direction.ASC, "pricePerMin");
        } else if (sortString.equals("distanceAsc") && hasLocation) {
            sort = Sort.by(Sort.Direction.ASC, "distanceKm");
        } else {
            sort = Sort.by("id");
        }

        pageRequest = pageRequest.withSort(sort);

        if (hasLocation) {
            Page<VehicleQuery> vehicles;
            if (hasText) {
                vehicles = vehicleRepository.findAllWithinRadiusAndName(lat, lon, radiusKm, VehicleStatus.AVAILABLE.name(), q, pageRequest);
            } else {
                vehicles = vehicleRepository.findAllWithinRadius(lat, lon, radiusKm, VehicleStatus.AVAILABLE.name(), pageRequest);
            }
            return vehicles.map(this::toResponseDto);
        } else {
            Page<Vehicle> vehicles;
            if (hasText) {
                vehicles = vehicleRepository.searchAvailable(q, pageRequest);
            } else {
                vehicles = vehicleRepository.findAllByStatus(VehicleStatus.AVAILABLE, pageRequest);
            }
            return vehicles.map(this::toResponseDto);
        }
    }

    private VehicleUserResponseDto toResponseDto(Vehicle vehicle) {
        VehicleModel model = vehicle.getModel();
        var modelName = model != null ? model.getModel() : null;
        var brand = model != null ? model.getBrand() : null;
        return VehicleUserResponseDto.builder()
                .id(vehicle.getId())
                .brand(brand)
                .modelName(modelName)
                .longitude(vehicle.getLongitude())
                .latitude(vehicle.getLatitude())
                .pricePerMin(vehicle.getPricePerMin())
                .type(vehicle.getType())
                .status(vehicle.getStatus())
                .distanceKm(null)
                .build();
    }

    private VehicleUserResponseDto toResponseDto(VehicleQuery vehicle) {
        System.out.println(vehicle);
        return VehicleUserResponseDto.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .modelName(vehicle.getModelName())
                .longitude(vehicle.getLongitude())
                .latitude(vehicle.getLatitude())
                .pricePerMin(vehicle.getPricePerMin())
                .type(vehicle.getType())
                .status(vehicle.getStatus())
                .distanceKm(vehicle.getDistanceKm())
                .build();
    }

    private VehicleAdminResponseDto toAdminResponseDto(Vehicle vehicle) {
        VehicleModel model =  vehicle.getModel();
        String modelName = null, brand = null;
        Long modelId = null;

        if (model != null) {
            modelId = model.getId();
            brand = model.getBrand();
            modelName = model.getModel();
        } else {
            System.out.println("model is null");
        }

        return VehicleAdminResponseDto.builder()
                .id(vehicle.getId())
                .modelId(modelId)
                .brand(brand)
                .modelName(modelName)
                .pricePerMin(vehicle.getPricePerMin())
                .type(vehicle.getType())
                .status(vehicle.getStatus())
                .latitude(vehicle.getLatitude())
                .longitude(vehicle.getLongitude())
                .build();
    }
}
