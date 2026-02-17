package com.team3.driveza.service;


import com.team3.driveza.model.VehicleModel;
import com.team3.driveza.repository.VehicleModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleModelService {
    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    // TODO: Use DTOs
    public List<VehicleModel> getAllModels() {
        return vehicleModelRepository.findAll();
    }

    public VehicleModel getModelById(long id) throws RuntimeException {
        return getModelWithoutDTO(id);
    }

    public VehicleModel createModel(VehicleModel newVehicleModel) {
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setModel(newVehicleModel.getModel());
        vehicleModel.setBrand(newVehicleModel.getBrand());
        return vehicleModel;
    }

    public VehicleModel updateModel(long id, VehicleModel newVehicleModel) throws RuntimeException {
        VehicleModel vehicleModel = getModelWithoutDTO(id);
        vehicleModel.setModel(newVehicleModel.getModel());
        vehicleModel.setBrand(newVehicleModel.getBrand());
        return vehicleModel;
    }

    public void deleteModel(long id) {
        VehicleModel vehicleModel = getModelWithoutDTO(id);
        vehicleModelRepository.delete(vehicleModel);
    }

    public VehicleModel getModelWithoutDTO(long id) throws RuntimeException {
        Optional<VehicleModel> optionalVehicleModel = vehicleModelRepository.findById(id);
        if (optionalVehicleModel.isEmpty()) {
            throw new RuntimeException("This vehicle model does not exist.");
        }
        return optionalVehicleModel.get();
    }
}