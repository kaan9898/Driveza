package com.team3.driveza.service;


import com.team3.driveza.exception.ResourceNotFoundException;
import com.team3.driveza.model.VehicleModel;
import com.team3.driveza.repository.VehicleModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleModelService {
    private final VehicleModelRepository vehicleModelRepository;

    // TODO: Use DTOs
    public List<VehicleModel> getAllModels() {
        return vehicleModelRepository.findAll();
    }

    public VehicleModel getModelById(long id) throws ResourceNotFoundException {
        return findOrThrow(id);
    }

    public void createModel(VehicleModel newVehicleModel) {
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setModel(newVehicleModel.getModel());
        vehicleModel.setBrand(newVehicleModel.getBrand());
        vehicleModelRepository.save(vehicleModel);
    }

    public void updateModel(long id, VehicleModel newVehicleModel) throws ResourceNotFoundException {
        VehicleModel vehicleModel = findOrThrow(id);
        vehicleModel.setModel(newVehicleModel.getModel());
        vehicleModel.setBrand(newVehicleModel.getBrand());
        vehicleModelRepository.save(vehicleModel);
    }

    public void deleteModel(long id) {
        VehicleModel vehicleModel = findOrThrow(id);
        vehicleModelRepository.delete(vehicleModel);
    }

    public VehicleModel findOrThrow(Long id) throws ResourceNotFoundException {
        return vehicleModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This vehicle model does not exist."));
    }

    public VehicleModel findOrCreate(String brand, String model){
        return vehicleModelRepository.findByBrandIgnoreCaseAndModelIgnoreCase(brand.trim(),model.trim())
                .orElseGet(()->{
                    VehicleModel vehicleModel = new VehicleModel();
                    vehicleModel.setBrand(brand.trim());
                    vehicleModel.setModel(model.trim());
                    return vehicleModelRepository.save(vehicleModel);
                });
    }
}