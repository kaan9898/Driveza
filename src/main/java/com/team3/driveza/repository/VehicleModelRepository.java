package com.team3.driveza.repository;

import com.team3.driveza.model.VehicleModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleModelRepository extends ListCrudRepository<VehicleModel, Long> {
    Optional<VehicleModel> findByBrandIgnoreCaseAndModelIgnoreCase(String brand, String model);
}
