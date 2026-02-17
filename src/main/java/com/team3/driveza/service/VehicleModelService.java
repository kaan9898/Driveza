package com.team3.driveza.service;


import com.team3.driveza.model.VehicleModel;
import com.team3.driveza.repository.VehicleModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleModelService {
    private final VehicleModelRepository vehicleModelRepository;

    // TODO: Use DTOs
    public List<VehicleModel> getAllModels() {
        return vehicleModelRepository.findAll();
    }

    public VehicleModel getModelById(long id) throws RuntimeException {
        return findOrThrow(id);
    }

    public VehicleModel createModel(VehicleModel newVehicleModel) {
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setModel(newVehicleModel.getModel());
        vehicleModel.setBrand(newVehicleModel.getBrand());
        return vehicleModelRepository.save(vehicleModel);
    }

    public VehicleModel updateModel(long id, VehicleModel newVehicleModel) throws RuntimeException {
        VehicleModel vehicleModel = findOrThrow(id);
        vehicleModel.setModel(newVehicleModel.getModel());
        vehicleModel.setBrand(newVehicleModel.getBrand());
        return vehicleModelRepository.save(vehicleModel);
    }

    public void deleteModel(long id) {
        VehicleModel vehicleModel = findOrThrow(id);
        vehicleModelRepository.delete(vehicleModel);
    }

    private VehicleModel findOrThrow(long id) throws RuntimeException {
        return vehicleModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This vehicle model does not exist."));
    }
}