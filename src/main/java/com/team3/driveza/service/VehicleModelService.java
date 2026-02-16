package com.team3.driveza.service;

import com.team3.driveza.model.VehicleModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleModelService {

    public List<VehicleModel> getAllModels() {
        return new ArrayList<>();
    }

    public VehicleModel getModelById(Long id) {
        return new VehicleModel();
    }

    public VehicleModel createModel(VehicleModel model) {
        return model;
    }

    public VehicleModel updateModel(Long id, VehicleModel model) {
        return model;
    }

    public void deleteModel(Long id) {
        // nothing for now
    }
}